package com.swissroute.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "SearchHistoryRequestDTO",
        description = "DTO interno utilizado para registrar una búsqueda de conexiones en el historial del usuario autenticado"
)
public class SearchHistoryRequestDTO {

    @Schema(
            description = "Origen consultado por el usuario",
            example = "Lausanne"
    )
    private String origen;

    @Schema(
            description = "Destino consultado por el usuario",
            example = "Bern"
    )
    private String destino;

    @Schema(
            description = "Cantidad de resultados obtenidos en la búsqueda de conexiones",
            example = "4"
    )
    private Integer numResultados;

    @Schema(
            description = "ID del usuario autenticado obtenido desde el JWT",
            example = "1",
            hidden = true
    )
    private Long userId;
}