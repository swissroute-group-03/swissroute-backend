package com.swissroute.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class StationResponseDTO {
    private String id;
    private String nombre;
    private Double latitud;
    private Double longitud;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double distance;
}
