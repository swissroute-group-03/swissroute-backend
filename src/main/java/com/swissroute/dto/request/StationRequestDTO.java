package com.swissroute.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para buscar estaciones por nombre")
public class StationRequestDTO {
    @NotBlank
    @Schema(description = "Texto de búsqueda", example = "Lausanne", required = true)
    private String query;
}
