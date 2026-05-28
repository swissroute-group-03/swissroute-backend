package com.swissroute.service.use_cases;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;

import java.util.List;

public interface FavoriteStationService {
    
    FavoriteStationResponseDTO createFavoriteStation(FavoriteStationRequestDTO request);

    List<FavoriteStationResponseDTO> getFavoriteStations();

    void deleteFavoriteStation(Long id);
}
