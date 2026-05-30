package com.swissroute.service;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ForbiddenException;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.use_cases.FavoriteRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FavoriteRouteServiceImpl implements FavoriteRouteService {

    private final WebClient transportWebClient;

    @Override
    public List<FavoriteRouteResponseDTO> getFavoriteRoutes() {
        try {
            Map<?, ?> response = transportWebClient.get()
                    .uri("/api/rutas-favoritas")
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

            List<Map<String, Object>> allItems = (List<Map<String, Object>>) response.get("routes");

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
    public FavoriteRouteResponseDTO updateFavoriteRoute(Long id, FavoriteRouteRequestDTO request) {
        try {
            Map<?, ?> response = transportWebClient.put()
                    .uri("/api/rutas-favoritas/{id}", id)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> {
                                if (resp.statusCode().value() == 403) {
                                    return Mono.error(new ForbiddenException(
                                            "Route id " + id + " does not belong to the authenticated user"));
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

        } catch (ForbiddenException e) {
            throw e;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unavailable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    @Override
    public void deleteFavoriteRoute(Long id) {
        try {
            transportWebClient.delete()
                    .uri("/api/rutas-favoritas/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            resp -> {
                                if (resp.statusCode().value() == 403) {
                                    return Mono.error(new ForbiddenException(
                                            "Route id " + id + " does not belong to the authenticated user"));
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
                    .bodyToMono(String.class)
                    .block();

        } catch (ForbiddenException e) {
            throw e;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new TransportApiException(503, "Service Unable",
                    "Error al consultar el servicio de transporte externo");
        }
    }

    private FavoriteRouteResponseDTO toResponseDTO(Map<String, Object> item) {
        return new FavoriteRouteResponseDTO(
                ((Number) item.get("id")).longValue(),
                (String) item.get("name"),
                (String) item.get("origin"),
                (String) item.get("destination"),
                (String) item.get("transportType")
        );
    }
}