package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
import com.swissroute.service.SearchHistoryServiceImpl;

class SearchHistoryServiceImplTest {

    private SearchHistoryRepository searchHistoryRepository;
    private UserRepository userRepository;
    private SearchHistoryMapper searchHistoryMapper;
    private SearchHistoryServiceImpl searchHistoryService;

    @BeforeEach
    void setUp() {
        searchHistoryRepository = mock(SearchHistoryRepository.class);
        userRepository = mock(UserRepository.class);
        searchHistoryMapper = mock(SearchHistoryMapper.class);
        searchHistoryService = new SearchHistoryServiceImpl(searchHistoryRepository, userRepository, searchHistoryMapper);
    }

    @Test
    void registerSearch_ShouldSaveHistory() {
        SearchHistoryRequestDTO request = new SearchHistoryRequestDTO();
        request.setUserId(1L);

        User user = new User();
        user.setId(1L);

        SearchHistory entity = new SearchHistory();

        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(searchHistoryMapper.toEntity(request)).thenReturn(entity);

        searchHistoryService.registerSearch(request);

        verify(searchHistoryRepository).save(entity);
        assertEquals(user, entity.getUser());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAuthenticatedUserHistory_ShouldReturnPageResponseDTO() {
        Long userId = 1L;
        SearchHistory history = new SearchHistory();
        Page<SearchHistory> page = new PageImpl<>(List.of(history));

        SearchHistoryResponseDTO dto = new SearchHistoryResponseDTO();

        when(searchHistoryRepository.findByUser_IdAndFlgState(eq(userId), eq(EstadoConstants.ACTIVE), any(Pageable.class)))
                .thenReturn(page);
        when(searchHistoryMapper.toDto(history)).thenReturn(dto);

        PageResponseDTO<SearchHistoryResponseDTO> result = searchHistoryService.getAuthenticatedUserHistory(userId, 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAuthenticatedUserHistory_ShouldThrowBadRequestException_WhenInvalidPage() {
        assertThrows(BadRequestException.class, 
                () -> searchHistoryService.getAuthenticatedUserHistory(1L, -1, 10));
    }

    @Test
    void getAuthenticatedUserHistory_ShouldThrowBadRequestException_WhenInvalidSize() {
        assertThrows(BadRequestException.class, 
                () -> searchHistoryService.getAuthenticatedUserHistory(1L, 0, 0));
        assertThrows(BadRequestException.class, 
                () -> searchHistoryService.getAuthenticatedUserHistory(1L, 0, 101));
    }

    @Test
    void deleteHistoryById_ShouldSetInactiveState_WhenHistoryExists() {
        Long id = 100L;
        Long userId = 1L;
        SearchHistory history = new SearchHistory();
        history.setFlgState(EstadoConstants.ACTIVE);

        when(searchHistoryRepository.findByIdAndUser_IdAndFlgState(id, userId, EstadoConstants.ACTIVE))
                .thenReturn(Optional.of(history));

        searchHistoryService.deleteHistoryById(id, userId);

        assertEquals(EstadoConstants.INACTIVE, history.getFlgState());
        verify(searchHistoryRepository).save(history);
    }

    @Test
    void deleteHistoryById_ShouldThrowResourceNotFoundException_WhenHistoryDoesNotExist() {
        when(searchHistoryRepository.findByIdAndUser_IdAndFlgState(any(), any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> searchHistoryService.deleteHistoryById(100L, 1L));
    }

    @Test
    void deleteAllHistory_ShouldSetAllToInactive() {
        Long userId = 1L;
        SearchHistory h1 = new SearchHistory();
        h1.setFlgState(EstadoConstants.ACTIVE);
        SearchHistory h2 = new SearchHistory();
        h2.setFlgState(EstadoConstants.ACTIVE);

        when(searchHistoryRepository.findAllByUser_IdAndFlgState(userId, EstadoConstants.ACTIVE))
                .thenReturn(List.of(h1, h2));

        searchHistoryService.deleteAllHistory(userId);

        assertEquals(EstadoConstants.INACTIVE, h1.getFlgState());
        assertEquals(EstadoConstants.INACTIVE, h2.getFlgState());
        verify(searchHistoryRepository).saveAll(any(List.class));
    }
}
