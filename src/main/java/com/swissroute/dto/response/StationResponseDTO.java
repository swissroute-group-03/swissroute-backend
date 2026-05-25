package com.swissroute.dto.response;

import lombok.Data;

@Data
public class StationResponseDTO {
    private String id;
    private String nombre;
    private Double latitud;
    private Double longitud;
}
