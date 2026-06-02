package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO que representa una sección individual dentro de una conexión")
public class SeccionDTO {
    @Schema(description = "Estación de origen de la sección", example = "Lausanne")
    private String origen;

    @Schema(description = "Estación de destino de la sección", example = "Genève")
    private String destino;

    @Schema(description = "Hora de salida", example = "2026-05-28T10:30:00")
    private String salida;

    @Schema(description = "Hora de llegada", example = "2026-05-28T11:12:00")
    private String llegada;

    @Schema(description = "Nombre de la línea o servicio de transporte", example = "IC1")
    private String transporte;

    @Schema(description = "Tipo de transporte", example = "train", allowableValues = {"train", "bus", "tram", "ship"})
    private String tipo;
}
