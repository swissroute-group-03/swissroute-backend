package com.swissroute.service.use_cases;

import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;

import java.util.List;

public interface FavoriteRouteService {
    List<FavoriteRouteResponseDTO> getFavoriteRoutes();

    FavoriteRouteResponseDTO updateFavoriteRoute(Long id, FavoriteRouteRequestDTO request);

    void deleteFavoriteRoute(Long id);
}
