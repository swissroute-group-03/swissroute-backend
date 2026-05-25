package com.swissroute.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StationRequestDTO {
    @NotBlank
    private String query;
}
