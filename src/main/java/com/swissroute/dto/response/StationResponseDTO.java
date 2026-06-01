package com.swissroute.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO con información de una estación")
public class StationResponseDTO {
    @Schema(description = "Identificador de la estación", example = "8501120")
    private String id;

    @Schema(description = "Nombre de la estación", example = "Lausanne")
    private String nombre;

    @Schema(description = "Latitud de la estación", example = "46.517")
    private Double latitud;

    @Schema(description = "Longitud de la estación", example = "6.634")
    private Double longitud;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Distancia desde el punto de búsqueda en metros", example = "150.5")
    private Double distance;
}
