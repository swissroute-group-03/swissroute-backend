package com.swissroute.service.use_cases;

import com.swissroute.dto.request.RutaFavoritaRequestDTO;
import com.swissroute.dto.response.RutaFavoritaResponseDTO;

public interface RutaFavoritaUseCase {

    RutaFavoritaResponseDTO guardarRutaFavorita(
            RutaFavoritaRequestDTO requestDTO,
            Long userId
    );
}