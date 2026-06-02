package com.swissroute.controller;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.request.FavoriteRouteCreateRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.dto.response.FavoriteRouteCreateResponseDTO;
import com.swissroute.exceptionHandler.ErrorDetails;
import com.swissroute.service.use_cases.FavoriteRouteService;
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

import java.util.List;

@RestController
@RequestMapping("/api/rutas-favoritas")
@RequiredArgsConstructor
@Tag(name = "Rutas Favoritas", description = "Endpoints para gestionar rutas favoritas del usuario autenticado")
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;

    @PostMapping
    @Operation(
        summary = "Guardar ruta favorita",
        description = "Persiste una nueva ruta favorita en base de datos local asociada al usuario autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Ruta favorita creada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FavoriteRouteCreateResponseDTO.class))
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

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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
