package com.swissroute.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta del historial de búsquedas")
public class SearchHistoryResponseDTO {

    private Long id;
    private String origen;
    private String destino;
    private LocalDateTime fechaConsulta;
    private Integer numResultados;
    private Long userId;
}