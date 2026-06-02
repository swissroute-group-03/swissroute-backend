package com.swissroute.exceptionHandler;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Estructura estándar de error de la API")
public class ErrorDetails {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(description = "Marca de tiempo del error", example = "28/05/2026 10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "404")
    private int status;

    @Schema(description = "Tipo de error", example = "Not Found")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Recurso no encontrado")
    private String message;

    @Schema(description = "Endpoint donde ocurrió el error", example = "/api/estaciones")
    private String path;
}
