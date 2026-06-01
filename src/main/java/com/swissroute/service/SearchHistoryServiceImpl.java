package com.swissroute.service;

import com.swissroute.constant.EstadoConstants;
import com.swissroute.dto.request.SearchHistoryRequestDTO;
import com.swissroute.dto.response.PageResponseDTO;
import com.swissroute.dto.response.SearchHistoryResponseDTO;
import com.swissroute.exceptionHandler.exceptions.BadRequestException;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.mapper.SearchHistoryMapper;
import com.swissroute.model.SearchHistory;
import com.swissroute.model.User;
import com.swissroute.repository.SearchHistoryRepository;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.use_cases.SearchHistoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryUseCase {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final SearchHistoryMapper searchHistoryMapper;

    @Override
    @Transactional
    public void registerSearch(SearchHistoryRequestDTO request) {
        User authenticatedUser = userRepository.getReferenceById(request.getUserId());

        SearchHistory searchHistory = searchHistoryMapper.toEntity(request);
        searchHistory.setUser(authenticatedUser);
        searchHistory.setFechaConsulta(LocalDateTime.now());

        searchHistoryRepository.save(searchHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<SearchHistoryResponseDTO> getAuthenticatedUserHistory(
            Long userId,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new BadRequestException("El número de página no puede ser negativo");
        }

        if (size <= 0 || size > 100) {
            throw new BadRequestException("El tamaño de página debe estar entre 1 y 100");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "fechaConsulta")
        );

        Page<SearchHistory> historyPage = searchHistoryRepository.findByUser_IdAndFlgState(
                userId,
                EstadoConstants.ACTIVE,
                pageable
        );

        return new PageResponseDTO<>(
                historyPage.getContent()
                        .stream()
                        .map(searchHistoryMapper::toDto)
                        .toList(),
                historyPage.getNumber(),
                historyPage.getSize(),
                historyPage.getTotalElements(),
                historyPage.getTotalPages(),
                historyPage.isLast()
        );
    }

    @Override
    @Transactional
    public void deleteHistoryById(Long id, Long userId) {
        SearchHistory searchHistory = searchHistoryRepository
                .findByIdAndUser_IdAndFlgState(id, userId, EstadoConstants.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el historial solicitado para el usuario autenticado"
                ));

        searchHistory.setFlgState(EstadoConstants.INACTIVE);

        searchHistoryRepository.save(searchHistory);
    }

    @Override
    @Transactional
    public void deleteAllHistory(Long userId) {
        List<SearchHistory> histories = searchHistoryRepository.findAllByUser_IdAndFlgState(
                userId,
                EstadoConstants.ACTIVE
        );

        histories.forEach(history -> history.setFlgState(EstadoConstants.INACTIVE));

        searchHistoryRepository.saveAll(histories);
    }
}