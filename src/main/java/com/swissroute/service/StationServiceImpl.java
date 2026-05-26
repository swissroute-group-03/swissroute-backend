package com.swissroute.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.dto.response.StationResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.use_cases.StationUseCase;

@Service
public class StationServiceImpl implements StationUseCase {

    private final WebClient transportWebClient;

    public StationServiceImpl(WebClient transportWebClient) {
        this.transportWebClient = transportWebClient;
    }

    @Override
    public List<StationResponseDTO> buscarEstaciones(String query, Integer limite) {
        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/locations")
                            .queryParam("query", query)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> allItems = (List<Map<String, Object>>) response.get("stations");

            if (allItems == null) {
                throw new ResourceNotFoundException("No se encontraron estaciones con el nombre: " + query);
            }

            List<Map<String, Object>> stations = allItems.stream()
                    .filter(s -> s.get("id") != null)
                    .toList();

            if (stations.isEmpty()) {
                throw new ResourceNotFoundException("No se encontraron estaciones con el nombre: " + query);
            }

            return stations.stream()
                    .map(s -> toResponseDTO(s))
                    .limit(limite)
                    .toList();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @Override
    public List<StationResponseDTO> buscarEstacionesPorCoordenadas(Double x, Double y, Integer limite) {
        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/locations")
                            .queryParam("x", x)
                            .queryParam("y", y)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<Map<String, Object>> allItems = (List<Map<String, Object>>) response.get("stations");

            if (allItems == null || allItems.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No se encontraron estaciones cerca de las coordenadas: " + x + ", " + y);
            }

            List<Map<String, Object>> stations = allItems.stream()
                    .filter(s -> s.get("id") != null)
                    .toList();

            if (stations.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No se encontraron estaciones cerca de las coordenadas: " + x + ", " + y);
            }

            return stations.stream()
                    .map(s -> toResponseDTO(s))
                    .sorted(Comparator.comparing(
                            StationResponseDTO::getDistance,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .limit(limite)
                    .toList();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @SuppressWarnings("unchecked")
    private StationResponseDTO toResponseDTO(Map<String, Object> s) {
        StationResponseDTO dto = new StationResponseDTO();
        dto.setId((String) s.get("id"));
        dto.setNombre((String) s.get("name"));
        Map<String, Object> coord = (Map<String, Object>) s.get("coordinate");
        if (coord != null) {
            dto.setLatitud((Double) coord.get("x"));
            dto.setLongitud((Double) coord.get("y"));
        }
        Object distance = s.get("distance");
        if (distance instanceof Number) {
            dto.setDistance(((Number) distance).doubleValue());
        }
        return dto;
    }
}
