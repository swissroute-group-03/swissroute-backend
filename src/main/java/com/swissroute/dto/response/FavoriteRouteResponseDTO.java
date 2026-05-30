package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record FavoriteRouteResponseDTO(
        @Schema(description = "Internal route id", example = "1")
        Long id,

        @Schema(description = "Route name", example = "Home to Work")
        String name,

        @Schema(description = "Origin station name", example = "Madrid Chamartin")
        String origin,

        @Schema(description = "Destination station name", example = "Barcelona Sants")
        String destination,

        @Schema(description = "Transport type", example = "train", allowableValues = {"train", "bus", "tram", "metro", "subway"})
        String transportType
) {
}
