package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;
import com.swissroute.service.use_cases.FavoriteStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteStationController {

    private final FavoriteStationService favoriteStationService;


    @PostMapping("/estaciones-favoritas")
    public ResponseEntity<FavoriteStationResponseDTO> createFavoriteStation(
            @RequestBody FavoriteStationRequestDTO request) {

        FavoriteStationResponseDTO created = favoriteStationService.createFavoriteStation(request);

        return ResponseEntity.ok(created);
    }

    @GetMapping("/estaciones-favoritas")
    public ResponseEntity<List<FavoriteStationResponseDTO>> getFavoriteStations() {

        List<FavoriteStationResponseDTO> favorites = favoriteStationService.getFavoriteStations();

        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/estaciones-favoritas/{id}")
    public ResponseEntity<Void> deleteFavoriteStation(@PathVariable Long id) {

        favoriteStationService.deleteFavoriteStation(id);

        return ResponseEntity.noContent().build();
    }
}