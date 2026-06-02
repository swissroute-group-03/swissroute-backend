package com.swissroute.service.use_cases;

import java.util.List;

import com.swissroute.dto.response.StationResponseDTO;

public interface StationUseCase {
    List<StationResponseDTO> buscarEstaciones(String query, Integer limite);
    List<StationResponseDTO> buscarEstacionesPorCoordenadas(Double x, Double y, Integer limite);
}
