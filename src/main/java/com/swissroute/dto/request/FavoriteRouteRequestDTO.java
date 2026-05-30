package com.swissroute.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record FavoriteRouteRequestDTO(
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