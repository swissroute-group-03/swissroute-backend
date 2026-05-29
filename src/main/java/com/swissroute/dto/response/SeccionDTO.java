package com.swissroute.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeccionDTO {
    private String origen;
    private String destino;
    private String salida;
    private String llegada;
    private String transporte;
    private String tipo;
}
