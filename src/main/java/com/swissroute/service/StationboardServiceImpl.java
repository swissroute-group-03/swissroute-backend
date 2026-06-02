package com.swissroute.service;

import com.swissroute.dto.response.StationboardDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.use_cases.StationboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationboardServiceImpl implements StationboardService {

    private final WebClient transportWebClient;

    @Override
    public List<StationboardDTO> getStationboard(
            String station,
            Integer limit,
            Optional<String> transportType) {

        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/stationboard")
                                .queryParam("station", station)
                                .queryParam("limit", limit);
                        if (transportType.isPresent()) {
                            builder.queryParam("transportations[]", transportType.get());
                        }
                        return builder.build();
                    })
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> Mono.error(new TransportApiException(
                                    400,
                                    "Invalid station or incorrect parameters",
                                    ""
                            )))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(new TransportApiException(
                                    500,
                                    "Error in transport server",
                                    ""
                            )))
                    .bodyToMono(Map.class)
                    .block();

            if (response == null) {
                throw new ResourceNotFoundException("No se recibieron datos de la estación: " + station);
            }

            List<Map<String, Object>> allItems = (List<Map<String, Object>>) response.get("stationboard");

            if (allItems == null || allItems.isEmpty()) {
                throw new ResourceNotFoundException("No se encontraron trenes/buses para la estación: " + station);
            }

            List<Map<String, Object>> items = allItems.stream()
                    .filter(item -> item.get("name") != null)
                    .collect(Collectors.toList());

            if (items.isEmpty()) {
                throw new ResourceNotFoundException("No se encontraron salidas para la estación: " + station);
            }

            return items.stream()
                    .map(this::toResponseDTO)
                    .limit(limit)
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo: " + e.getMessage());
        }
    }


    private StationboardDTO toResponseDTO(Map<String, Object> item) {
        Map<String, Object> stop = (Map<String, Object>) item.get("stop");
        String departureTimeStr = null;
        if (stop != null) {
            departureTimeStr = (String) stop.get("departure");
        }

        LocalDateTime departureTime = null;
        if (departureTimeStr != null) {
            try {
                OffsetDateTime odt = OffsetDateTime.parse(departureTimeStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                departureTime = odt.toLocalDateTime();
            } catch (Exception e) {
                try {
                    departureTime = LocalDateTime.parse(departureTimeStr);
                } catch (Exception ex) {
                    departureTime = LocalDateTime.now();
                }
            }
        }

        return new StationboardDTO(
                (String) item.get("name"),
                (String) item.get("category"),
                (String) item.get("to"),
                departureTime
        );
    }
}