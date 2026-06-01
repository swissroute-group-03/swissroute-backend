package com.swissroute.service.use_cases;

import com.swissroute.dto.request.SearchHistoryRequestDTO;
import com.swissroute.dto.response.PageResponseDTO;
import com.swissroute.dto.response.SearchHistoryResponseDTO;

public interface SearchHistoryUseCase {

    void registerSearch(SearchHistoryRequestDTO request);

    PageResponseDTO<SearchHistoryResponseDTO> getAuthenticatedUserHistory(
            Long userId,
            int page,
            int size
    );

    void deleteHistoryById(Long id, Long userId);

    void deleteAllHistory(Long userId);
}