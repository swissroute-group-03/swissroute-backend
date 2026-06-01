package com.swissroute.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swissroute.dto.response.ConnectionResponseDTO;
import com.swissroute.exceptionHandler.ErrorDetails;
import com.swissroute.exceptionHandler.exceptions.BadRequestException;
import com.swissroute.service.use_cases.ConnectionUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/conexiones")
@Tag(name = "Conexiones", description = "Consulta de conexiones entre estaciones de transporte público")
public class ConnectionController {

    private static final int MAX_VIAS = 5;

    private final ConnectionUseCase connectionUseCase;

    public ConnectionController(ConnectionUseCase connectionUseCase) {
        this.connectionUseCase = connectionUseCase;
    }

    @GetMapping
    @Operation(
        summary = "Buscar conexiones",
        description = "Busca conexiones entre dos estaciones con filtros opcionales de fecha, hora, tipo de transporte y paradas intermedias"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de conexiones encontradas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConnectionResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos (ej. más de 5 paradas intermedias)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<List<ConnectionResponseDTO>> buscarConexiones(
            @Parameter(description = "Estación de origen", example = "Lausanne", required = true)
            @RequestParam String from,
            @Parameter(description = "Estación de destino", example = "Genève", required = true)
            @RequestParam String to,
            @Parameter(description = "Fecha del viaje (formato: YYYY-MM-DD)", example = "2026-06-15")
            @RequestParam(required = false) String date,
            @Parameter(description = "Hora del viaje (formato: HH:mm)", example = "14:30")
            @RequestParam(required = false) String time,
            @Parameter(description = "Tipos de transporte permitidos separados por coma", example = "train,bus")
            @RequestParam(required = false) String transportations,
            @Parameter(description = "Paradas intermedias (máximo 5)", example = "[\"Bern\",\"Zürich\"]")
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
