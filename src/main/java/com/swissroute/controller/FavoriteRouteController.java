package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.exceptionHandler.ErrorDetails;
import com.swissroute.service.use_cases.FavoriteRouteService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Rutas Favoritas", description = "Endpoints para consultar, actualizar y eliminar rutas favoritas (proxy a API externa)")
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;


    @GetMapping("/rutas-favoritas")
    @Operation(
        summary = "Obtener rutas favoritas",
        description = "Lista las rutas favoritas del usuario autenticado mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de rutas favoritas",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteRouteResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Servicio externo no disponible",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<List<FavoriteRouteResponseDTO>> getFavoriteRoutes() {

        List<FavoriteRouteResponseDTO> routes = favoriteRouteService.getFavoriteRoutes();

        return ResponseEntity.ok(routes);
    }

    @PutMapping("/rutas-favoritas/{id}")
    @Operation(
        summary = "Actualizar ruta favorita",
        description = "Actualiza una ruta favorita existente mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Ruta favorita actualizada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteRouteResponseDTO.class))
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
        ),
        @ApiResponse(
            responseCode = "403",
            description = "La ruta no pertenece al usuario autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ruta favorita no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Servicio externo no disponible",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<FavoriteRouteResponseDTO> updateFavoriteRoute(
            @PathVariable Long id,
            @RequestBody FavoriteRouteRequestDTO request) {

        FavoriteRouteResponseDTO updated = favoriteRouteService.updateFavoriteRoute(id, request);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/rutas-favoritas/{id}")
    @Operation(
        summary = "Eliminar ruta favorita",
        description = "Elimina una ruta favorita mediante API externa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Ruta favorita eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "La ruta no pertenece al usuario autenticado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Ruta favorita no encontrada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Servicio externo no disponible",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDetails.class))
        )
    })
    public ResponseEntity<Void> deleteFavoriteRoute(@PathVariable Long id) {
        favoriteRouteService.deleteFavoriteRoute(id);
        return ResponseEntity.noContent().build();
    }
}