package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.request.FavoriteRouteCreateRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.dto.response.FavoriteRouteCreateResponseDTO;
import com.swissroute.service.use_cases.FavoriteRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutas-favoritas")
@RequiredArgsConstructor
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;


    @PostMapping
    public ResponseEntity<FavoriteRouteCreateResponseDTO> guardarRutaFavorita(
            @Valid @RequestBody FavoriteRouteCreateRequestDTO requestDTO,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        FavoriteRouteCreateResponseDTO response = favoriteRouteService.guardarRutaFavorita(
                requestDTO,
                userId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteRouteResponseDTO>> getFavoriteRoutes() {

        List<FavoriteRouteResponseDTO> routes = favoriteRouteService.getFavoriteRoutes();

        return ResponseEntity.ok(routes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FavoriteRouteResponseDTO> updateFavoriteRoute(
            @PathVariable Long id,
            @RequestBody FavoriteRouteRequestDTO request) {

        FavoriteRouteResponseDTO updated = favoriteRouteService.updateFavoriteRoute(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoriteRoute(@PathVariable Long id) {
        favoriteRouteService.deleteFavoriteRoute(id);
        return ResponseEntity.noContent().build();
    }
}