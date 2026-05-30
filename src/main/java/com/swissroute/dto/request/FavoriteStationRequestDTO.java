package com.swissroute.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FavoriteStationRequestDTO(

        @Schema(description = "Identificador externo de la estación", example = "12345")
        String externalStationId,

        @Schema(description = "Nombre de la estación", example = "Madrid Chamartín")
        String stationName
) {
}
