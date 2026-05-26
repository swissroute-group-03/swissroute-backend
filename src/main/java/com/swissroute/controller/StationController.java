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

@RestController
@RequestMapping("/api/estaciones")
public class StationController {

    private final StationUseCase stationUseCase;

    public StationController(StationUseCase stationUseCase) {
        this.stationUseCase = stationUseCase;
    }

    @GetMapping
    public ResponseEntity<?> buscarEstaciones(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Double x,
            @RequestParam(required = false) Double y,
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
