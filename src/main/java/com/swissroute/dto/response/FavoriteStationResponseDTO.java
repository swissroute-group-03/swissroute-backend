package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FavoriteStationResponseDTO(

        @Schema(description = "ID de la estación favorita", example = "1")
        Long id,

        @Schema(description = "Identificador externo de la estación", example = "12345")
        String externalStationId,

        @Schema(description = "Nombre de la estación", example = "Madrid Chamartín")
        String stationName
) {
}