package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.constant.EstadoConstants;
import com.swissroute.dto.request.FavoriteRouteCreateRequestDTO;
import com.swissroute.dto.request.FavoriteRouteRequestDTO;
import com.swissroute.dto.response.FavoriteRouteCreateResponseDTO;
import com.swissroute.dto.response.FavoriteRouteResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ConflictException;
import com.swissroute.exceptionHandler.exceptions.ForbiddenException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.mapper.FavoriteRouteMapper;
import com.swissroute.model.FavoriteRoute;
import com.swissroute.model.User;
import com.swissroute.repository.FavoriteRouteRepository;
import com.swissroute.repository.UserRepository;
import com.swissroute.service.FavoriteRouteServiceImpl;

import reactor.core.publisher.Mono;

class FavoriteRouteServiceImplTest {

    private WebClient transportWebClient;
    private FavoriteRouteRepository favoriteRouteRepository;
    private UserRepository userRepository;
    private FavoriteRouteMapper rutaFavoritaMapper;
    private FavoriteRouteServiceImpl favoriteRouteService;

    @BeforeEach
    void setUp() {
        transportWebClient = mock(WebClient.class);
        favoriteRouteRepository = mock(FavoriteRouteRepository.class);
        userRepository = mock(UserRepository.class);
        rutaFavoritaMapper = mock(FavoriteRouteMapper.class);
        favoriteRouteService = new FavoriteRouteServiceImpl(transportWebClient, favoriteRouteRepository, userRepository, rutaFavoritaMapper);
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientGet(Map<String, Object> response) {
        WebClient.RequestHeadersUriSpec mockGetUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);
        when(transportWebClient.get()).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.uri(any(String.class))).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientPut(Map<String, Object> response) {
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.put()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri(any(String.class), anyLong())).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.bodyValue(any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));
    }

    @Test
    void getFavoriteRoutes_ShouldReturnList_WhenApiReturns200() {
        Map<String, Object> route = Map.of(
                "id", 1,
                "name", "Work",
                "origin", "Lausanne",
                "destination", "Geneva",
                "transportType", "train"
        );
        mockWebClientGet(Map.of("routes", List.of(route)));

        List<FavoriteRouteResponseDTO> result = favoriteRouteService.getFavoriteRoutes();

        assertEquals(1, result.size());
        assertEquals("Work", result.get(0).name());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getFavoriteRoutes_ShouldThrowTransportApiException_WhenApiFails() {
        WebClient.RequestHeadersUriSpec mockGetUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);
        when(transportWebClient.get()).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.uri(any(String.class))).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException()));

        assertThrows(TransportApiException.class, () -> favoriteRouteService.getFavoriteRoutes());
    }

    @Test
    void updateFavoriteRoute_ShouldReturnUpdatedDTO() {
        FavoriteRouteRequestDTO request = new FavoriteRouteRequestDTO("New Name", "L", "G", "bus");
        Map<String, Object> response = Map.of(
                "id", 1,
                "name", "New Name",
                "origin", "L",
                "destination", "G",
                "transportType", "bus"
        );
        mockWebClientPut(response);

        FavoriteRouteResponseDTO result = favoriteRouteService.updateFavoriteRoute(1L, request);

        assertEquals("New Name", result.name());
    }

    @Test
    @SuppressWarnings("unchecked")
    void updateFavoriteRoute_ShouldHandleForbiddenError() {
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.put()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri(any(String.class), anyLong())).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.bodyValue(any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new ForbiddenException("Forbidden")));

        assertThrows(ForbiddenException.class, () -> favoriteRouteService.updateFavoriteRoute(1L, mock(FavoriteRouteRequestDTO.class)));
    }

    @Test
    void guardarRutaFavorita_ShouldSave_WhenDataIsValid() {
        FavoriteRouteCreateRequestDTO request = new FavoriteRouteCreateRequestDTO(
                "Home", "8503000", "L", "8503001", "G", "train");
        Long userId = 1L;
        FavoriteRoute entity = new FavoriteRoute();
        FavoriteRoute savedEntity = new FavoriteRoute();
        FavoriteRouteCreateResponseDTO responseDTO = new FavoriteRouteCreateResponseDTO();

        when(favoriteRouteRepository.existsByUser_IdAndNombreIgnoreCaseAndFlgState(userId, "Home", EstadoConstants.ACTIVE)).thenReturn(false);
        when(rutaFavoritaMapper.toEntity(request)).thenReturn(entity);
        when(userRepository.getReferenceById(userId)).thenReturn(new User());
        when(favoriteRouteRepository.save(entity)).thenReturn(savedEntity);
        when(rutaFavoritaMapper.toDto(savedEntity)).thenReturn(responseDTO);

        FavoriteRouteCreateResponseDTO result = favoriteRouteService.guardarRutaFavorita(request, userId);

        assertEquals(responseDTO, result);
        verify(favoriteRouteRepository).save(entity);
    }

    @Test
    void guardarRutaFavorita_ShouldThrowConflict_WhenNameExists() {
        FavoriteRouteCreateRequestDTO request = new FavoriteRouteCreateRequestDTO(
                "Home", "8503000", "L", "8503001", "G", "train");
        when(favoriteRouteRepository.existsByUser_IdAndNombreIgnoreCaseAndFlgState(1L, "Home", EstadoConstants.ACTIVE)).thenReturn(true);

        assertThrows(ConflictException.class, () -> favoriteRouteService.guardarRutaFavorita(request, 1L));
    }
}
