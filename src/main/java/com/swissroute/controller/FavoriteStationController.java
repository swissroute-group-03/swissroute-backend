package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;
import com.swissroute.exceptionHandler.ErrorDetails;
import com.swissroute.service.use_cases.FavoriteStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Estaciones Favoritas", description = "CRUD de estaciones favoritas del usuario mediante API externa")
public class FavoriteStationController {

    private final FavoriteStationService favoriteStationService;


    @PostMapping("/estaciones-favoritas")
    @Operation(
        summary = "Crear estación favorita",
        description = "Guarda una nueva estación favorita mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estación favorita creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteStationResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Solicitud inválida",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<FavoriteStationResponseDTO> createFavoriteStation(
            @RequestBody FavoriteStationRequestDTO request) {

        FavoriteStationResponseDTO created = favoriteStationService.createFavoriteStation(request);

        return ResponseEntity.ok(created);
    }

    @GetMapping("/estaciones-favoritas")
    @Operation(
        summary = "Obtener estaciones favoritas",
        description = "Lista las estaciones favoritas del usuario mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de estaciones favoritas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteStationResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<List<FavoriteStationResponseDTO>> getFavoriteStations() {

        List<FavoriteStationResponseDTO> favorites = favoriteStationService.getFavoriteStations();

        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/estaciones-favoritas/{id}")
    @Operation(
        summary = "Eliminar estación favorita",
        description = "Elimina una estación favorita por ID mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Estación favorita eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estación favorita no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<Void> deleteFavoriteStation(@PathVariable Long id) {

        favoriteStationService.deleteFavoriteStation(id);

        return ResponseEntity.noContent().build();
    }
}