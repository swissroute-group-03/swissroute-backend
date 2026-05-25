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
    public ResponseEntity<?> buscarEstaciones(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "El parametro 'query' es obligatorio"));
        }
        List<StationResponseDTO> stations = stationUseCase.buscarEstaciones(query);
        return ResponseEntity.ok(stations);
    }
}
