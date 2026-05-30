package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta de una ruta favorita")
public class RutaFavoritaResponseDTO {

    @Schema(description = "ID único de la ruta favorita", example = "1")
    private Long id;

    @Schema(description = "Nombre personalizado de la ruta favorita", example = "Ruta a la universidad")
    private String nombre;

    @Schema(description = "ID de la estación de origen según la API externa", example = "008501120")
    private String origenId;

    @Schema(description = "Nombre de la estación de origen", example = "Lausanne")
    private String origenNombre;

    @Schema(description = "ID de la estación de destino según la API externa", example = "008501008")
    private String destinoId;

    @Schema(description = "Nombre de la estación de destino", example = "Genève")
    private String destinoNombre;

    @Schema(description = "Tipo de transporte preferido", example = "train")
    private String tipoTransporte;

    @Schema(description = "ID del usuario autenticado al que pertenece la ruta favorita", example = "1")
    private Long userId;

    @Schema(description = "Fecha de creación del registro", example = "2026-05-28T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización del registro", example = "2026-05-28T11:00:00")
    private LocalDateTime updatedAt;
}