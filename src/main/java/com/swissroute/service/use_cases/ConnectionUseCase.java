package com.swissroute.service.use_cases;

import java.util.List;

import com.swissroute.dto.response.ConnectionResponseDTO;

public interface ConnectionUseCase {
    List<ConnectionResponseDTO> buscarConexiones(String from, String to, String date, String time, String transportations);
}
