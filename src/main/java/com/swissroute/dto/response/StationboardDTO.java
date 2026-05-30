package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record StationboardDTO(

        @Schema(description = "Nombre del servicio de transporte", example = "RE9")
        String serviceName,

        @Schema(
                description = "Categoría del transporte",
                example = "train",
                allowableValues = {"train", "bus", "tram", "metro", "subway"}
        )
        String category,

        @Schema(description = "Destino final del servicio", example = "Madrid Chamartín")
        String finalDestination,

        @Schema(description = "Hora programada de salida", example = "2026-05-28T10:30:00")
        LocalDateTime departureTime
) {
}