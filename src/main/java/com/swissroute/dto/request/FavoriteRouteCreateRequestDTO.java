package com.swissroute.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para registrar o actualizar una ruta favorita")
public class FavoriteRouteCreateRequestDTO {

    @NotBlank(message = "El nombre de la ruta favorita no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    @Schema(
            description = "Nombre personalizado de la ruta favorita",
            example = "Ruta a la universidad",
            required = true
    )
    private String nombre;

    @NotBlank(message = "El ID del origen no puede estar vacío")
    @Size(max = 50, message = "El ID del origen no puede superar los 50 caracteres")
    @Schema(
            description = "ID de la estación de origen según la API externa",
            example = "008501120",
            required = true
    )
    private String origenId;

    @NotBlank(message = "El nombre del origen no puede estar vacío")
    @Size(max = 255, message = "El nombre del origen no puede superar los 255 caracteres")
    @Schema(
            description = "Nombre de la estación de origen",
            example = "Lausanne",
            required = true
    )
    private String origenNombre;

    @NotBlank(message = "El ID del destino no puede estar vacío")
    @Size(max = 50, message = "El ID del destino no puede superar los 50 caracteres")
    @Schema(
            description = "ID de la estación de destino según la API externa",
            example = "008501008",
            required = true
    )
    private String destinoId;

    @NotBlank(message = "El nombre del destino no puede estar vacío")
    @Size(max = 255, message = "El nombre del destino no puede superar los 255 caracteres")
    @Schema(
            description = "Nombre de la estación de destino",
            example = "Genève",
            required = true
    )
    private String destinoNombre;

    @NotBlank(message = "El tipo de transporte no puede estar vacío")
    @Size(max = 100, message = "El tipo de transporte no puede superar los 100 caracteres")
    @Pattern(
            regexp = "(?i)train|tram|ship|bus|cableway",
            message = "El tipo de transporte debe ser: train, tram, ship, bus o cableway"
    )
    @Schema(
            description = "Tipo de transporte preferido para la ruta",
            example = "train",
            allowableValues = {"train", "tram", "ship", "bus", "cableway"},
            required = true
    )
    private String tipoTransporte;
}