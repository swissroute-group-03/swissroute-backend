package com.swissroute.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swissroute.dto.response.ConnectionResponseDTO;
import com.swissroute.service.use_cases.ConnectionUseCase;

@RestController
@RequestMapping("/api/conexiones")
public class ConnectionController {

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
            @RequestParam(required = false) String transportations) {

        List<ConnectionResponseDTO> conexiones = connectionUseCase.buscarConexiones(from, to, date, time, transportations);
        return ResponseEntity.ok(conexiones);
    }
}
