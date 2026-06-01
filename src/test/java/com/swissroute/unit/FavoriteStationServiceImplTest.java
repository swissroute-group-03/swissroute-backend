package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;
import com.swissroute.exceptionHandler.exceptions.FavoriteStationNotFoundException;
import com.swissroute.service.FavoriteStationServiceImpl;

import reactor.core.publisher.Mono;

class FavoriteStationServiceImplTest {

    private WebClient transportWebClient;
    private FavoriteStationServiceImpl favoriteStationService;

    @BeforeEach
    void setUp() {
        transportWebClient = mock(WebClient.class);
        favoriteStationService = new FavoriteStationServiceImpl(transportWebClient);
    }

    @SuppressWarnings("unchecked")
    @Test
    void createFavoriteStation_ShouldReturnDTO() {
        FavoriteStationRequestDTO request = new FavoriteStationRequestDTO("8503000", "Zurich HB");
        Map<String, Object> response = Map.of(
                "id", 1,
                "externalStationId", "8503000",
                "stationName", "Zurich HB"
        );

        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri(any(String.class))).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.bodyValue(any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));

        FavoriteStationResponseDTO result = favoriteStationService.createFavoriteStation(request);

        assertEquals("Zurich HB", result.stationName());
        assertEquals(1L, result.id());
    }

    @Test
    @SuppressWarnings("unchecked")
    void createFavoriteStation_ShouldHandleConflict() {
        WebClient.RequestBodyUriSpec mockRequestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec mockRequestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec mockRequestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.post()).thenReturn(mockRequestBodyUriSpec);
        when(mockRequestBodyUriSpec.uri(any(String.class))).thenReturn(mockRequestBodySpec);
        when(mockRequestBodySpec.bodyValue(any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new FavoriteStationNotFoundException("Conflict")));

        assertThrows(FavoriteStationNotFoundException.class, () -> favoriteStationService.createFavoriteStation(mock(FavoriteStationRequestDTO.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void getFavoriteStations_ShouldReturnList() {
        Map<String, Object> station = Map.of(
                "id", 1,
                "externalStationId", "8503000",
                "stationName", "Zurich HB"
        );
        WebClient.RequestHeadersUriSpec mockGetUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.get()).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.uri(any(String.class))).thenReturn(mockGetUriSpec);
        when(mockGetUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("favorites", List.of(station))));

        List<FavoriteStationResponseDTO> result = favoriteStationService.getFavoriteStations();

        assertEquals(1, result.size());
        assertEquals("Zurich HB", result.get(0).stationName());
    }
}
