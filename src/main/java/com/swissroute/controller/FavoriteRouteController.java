package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.service.use_cases.FavoriteRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;


    @GetMapping("/rutas-favoritas")
    public ResponseEntity<List<FavoriteRouteResponseDTO>> getFavoriteRoutes() {

        List<FavoriteRouteResponseDTO> routes = favoriteRouteService.getFavoriteRoutes();

        return ResponseEntity.ok(routes);
    }

    @PutMapping("/rutas-favoritas/{id}")
    public ResponseEntity<FavoriteRouteResponseDTO> updateFavoriteRoute(
            @PathVariable Long id,
            @RequestBody FavoriteRouteRequestDTO request) {

        FavoriteRouteResponseDTO updated = favoriteRouteService.updateFavoriteRoute(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/rutas-favoritas/{id}")
    public ResponseEntity<Void> deleteFavoriteRoute(@PathVariable Long id) {
        favoriteRouteService.deleteFavoriteRoute(id);
        return ResponseEntity.noContent().build();
    }
}