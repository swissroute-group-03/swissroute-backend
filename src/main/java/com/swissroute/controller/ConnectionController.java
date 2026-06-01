package com.swissroute.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swissroute.dto.response.ConnectionResponseDTO;
import com.swissroute.exceptionHandler.exceptions.BadRequestException;
import com.swissroute.service.use_cases.ConnectionUseCase;

@RestController
@RequestMapping("/api/conexiones")
public class ConnectionController {

    private static final int MAX_VIAS = 5;

    private final ConnectionUseCase connectionUseCase;

    public ConnectionController(ConnectionUseCase connectionUseCase) {
        this.connectionUseCase = connectionUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ConnectionResponseDTO>> buscarConexiones(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String transportations,
            @RequestParam(required = false) List<String> via,
            Authentication authentication) {

        List<String> viaFiltered = via;
        if (viaFiltered != null) {
            viaFiltered = via.stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            if (viaFiltered.isEmpty()) {
                viaFiltered = null;
            } else if (viaFiltered.size() > MAX_VIAS) {
                throw new BadRequestException(
                        "Se permiten máximo " + MAX_VIAS + " paradas intermedias");
            }
        }

        Long userId = (Long) authentication.getDetails();

        List<ConnectionResponseDTO> conexiones = connectionUseCase.buscarConexiones(from, to, date, time, transportations, viaFiltered, userId);
        return ResponseEntity.ok(conexiones);
    }
}
