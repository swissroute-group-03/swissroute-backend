package com.swissroute.controller;

import com.swissroute.dto.request.RutaFavoritaRequestDTO;
import com.swissroute.dto.response.RutaFavoritaResponseDTO;
import com.swissroute.exceptionHandler.ErrorDetails;
import com.swissroute.service.use_cases.RutaFavoritaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rutas-favoritas")
@RequiredArgsConstructor
@Tag(name = "Rutas Favoritas", description = "Endpoints para gestionar rutas favoritas del usuario autenticado (persistencia local)")
public class RutaFavoritaController {

    private final RutaFavoritaUseCase rutaFavoritaUseCase;

    @PostMapping
    @Operation(
        summary = "Guardar ruta favorita",
        description = "Persiste una nueva ruta favorita en base de datos local asociada al usuario autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Ruta favorita creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RutaFavoritaResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Ya existe una ruta favorita con ese nombre para el usuario",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<RutaFavoritaResponseDTO> guardarRutaFavorita(
            @Valid @RequestBody RutaFavoritaRequestDTO requestDTO,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        RutaFavoritaResponseDTO response = rutaFavoritaUseCase.guardarRutaFavorita(
                requestDTO,
                userId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}