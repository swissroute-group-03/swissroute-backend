package com.swissroute.service;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;
import com.swissroute.exceptionHandler.exceptions.FavoriteStationNotFoundException;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.use_cases.FavoriteStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class FavoriteStationServiceImpl implements FavoriteStationService {

    private final WebClient transportWebClient;

    @Override
    public FavoriteStationResponseDTO createFavoriteStation(FavoriteStationRequestDTO request) {
        try {
            Map<?, ?> response = transportWebClient.post()
                    .uri("/api/estaciones-favoritas")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> {
                                if (resp.statusCode().value() == 409) {
                                    return Mono.error(new FavoriteStationNotFoundException(
                                            "Station already exists in favorites: " + request.stationName()));
                                }
                                return Mono.error(new TransportApiException(
                                        resp.statusCode().value(),
                                        "Invalid request",
                                        ""));
                            })
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(new TransportApiException(
                                    500,
                                    "Error in server",
                                    "")))
                    .bodyToMono(Map.class)
                    .block();

            return toResponseDTO((Map<String, Object>) response);

        } catch (FavoriteStationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @Override
    public List<FavoriteStationResponseDTO> getFavoriteStations() {
        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri("/api/estaciones-favoritas")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> Mono.error(new TransportApiException(
                                    resp.statusCode().value(),
                                    "Invalid request",
                                    "")))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(new TransportApiException(
                                    500,
                                    "Error in server",
                                    "")))
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> allItems = (List<Map<String, Object>>) response.get("favorites");

            if (allItems == null || allItems.isEmpty()) {
                return List.of();
            }

            return allItems.stream()
                    .map(this::toResponseDTO)
                    .toList();

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @Override
    public void deleteFavoriteStation(Long id) {
        try {
            transportWebClient.delete()
                    .uri("/api/estaciones-favoritas/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> Mono.error(new TransportApiException(
                                    resp.statusCode().value(),
                                    "Invalid request",
                                    "")))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            resp -> Mono.error(new TransportApiException(
                                    500,
                                    "Error in server",
                                    "")))
                    .bodyToMono(String.class)
                    .block();

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    private FavoriteStationResponseDTO toResponseDTO(Map<String, Object> item) {
        return new FavoriteStationResponseDTO(
                ((Number) item.get("id")).longValue(),
                (String) item.get("externalStationId"),
                (String) item.get("stationName")
        );
    }
}