package com.swissroute.controller;

import com.swissroute.dto.response.PageResponseDTO;
import com.swissroute.dto.response.SearchHistoryResponseDTO;
import com.swissroute.service.use_cases.SearchHistoryUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@Tag(name = "Historial", description = "Endpoints para gestionar el historial de búsquedas")
public class SearchHistoryController {

    private final SearchHistoryUseCase searchHistoryUseCase;

    @GetMapping
    public ResponseEntity<PageResponseDTO<SearchHistoryResponseDTO>> getHistory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        PageResponseDTO<SearchHistoryResponseDTO> response =
                searchHistoryUseCase.getAuthenticatedUserHistory(userId, page, size);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoryById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getDetails();

        searchHistoryUseCase.deleteHistoryById(id, userId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllHistory(Authentication authentication) {
        Long userId = (Long) authentication.getDetails();

        searchHistoryUseCase.deleteAllHistory(userId);

        return ResponseEntity.noContent().build();
    }
}