package com.swissroute.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO con información de una conexión entre estaciones")
public class ConnectionResponseDTO {
    @Schema(description = "Estación de origen", example = "Lausanne")
    private String origen;

    @Schema(description = "Estación de destino", example = "Genève")
    private String destino;

    @Schema(description = "Duración total del viaje", example = "00:42:00")
    private String duracion;

    @Schema(description = "Productos de transporte disponibles", example = "[\"train\",\"bus\"]")
    private List<String> productos;

    @Schema(description = "Secciones que componen la conexión")
    private List<SeccionDTO> secciones;
}
