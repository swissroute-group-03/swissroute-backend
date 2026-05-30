package com.swissroute.controller;

import com.swissroute.dto.request.RutaFavoritaRequestDTO;
import com.swissroute.dto.response.RutaFavoritaResponseDTO;
import com.swissroute.service.use_cases.RutaFavoritaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rutas-favoritas")
@RequiredArgsConstructor
@Tag(name = "Rutas Favoritas", description = "Endpoints para gestionar rutas favoritas del usuario autenticado")
public class RutaFavoritaController {

    private final RutaFavoritaUseCase rutaFavoritaUseCase;

    @PostMapping
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