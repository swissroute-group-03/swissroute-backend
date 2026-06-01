package com.swissroute.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swissroute.dto.response.StationResponseDTO;
import com.swissroute.service.use_cases.StationUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/estaciones")
@Tag(name = "Estaciones", description = "Búsqueda de estaciones por nombre o coordenadas geográficas")
public class StationController {

    private final StationUseCase stationUseCase;

    public StationController(StationUseCase stationUseCase) {
        this.stationUseCase = stationUseCase;
    }

    @GetMapping
    @Operation(
        summary = "Buscar estaciones",
        description = "Busca estaciones por nombre o por coordenadas geográficas (latitud/longitud). No se pueden enviar ambos criterios simultáneamente."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de estaciones encontradas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StationResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos (coordenadas fuera de rango, query y coordenadas simultáneos, o falta de parámetros)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<?> buscarEstaciones(
            @Parameter(description = "Texto de búsqueda por nombre de estación", example = "Lausanne")
            @RequestParam(required = false) String query,
            @Parameter(description = "Latitud para búsqueda por coordenadas (debe estar entre -90 y 90)", example = "46.517")
            @RequestParam(required = false) Double x,
            @Parameter(description = "Longitud para búsqueda por coordenadas (debe estar entre -180 y 180)", example = "6.634")
            @RequestParam(required = false) Double y,
            @Parameter(description = "Cantidad máxima de resultados", example = "15")
            @RequestParam(required = false, defaultValue = "15") Integer limite) {

        if (x != null && y != null) {
            if (query != null && !query.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "No puede enviar 'query' junto con las coordenadas 'x' e 'y'"));
            }
            if (x < -90 || x > 90) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "El parámetro 'x' (latitud) debe estar entre -90 y 90"));
            }
            if (y < -180 || y > 180) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "El parámetro 'y' (longitud) debe estar entre -180 y 180"));
            }
            List<StationResponseDTO> stations = stationUseCase.buscarEstacionesPorCoordenadas(x, y, limite);
            return ResponseEntity.ok(stations);
        }

        if (query != null && !query.isBlank()) {
            List<StationResponseDTO> stations = stationUseCase.buscarEstaciones(query, limite);
            return ResponseEntity.ok(stations);
        }

        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "Debe proporcionar 'query' o las coordenadas 'x' e 'y'"));
    }
}
