package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.dto.response.StationResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.StationServiceImpl;

import reactor.core.publisher.Mono;

class StationServiceImplTest {

    private WebClient transportWebClient;
    private StationServiceImpl stationService;

    @BeforeEach
    void setUp() {
        transportWebClient = mock(WebClient.class);
        stationService = new StationServiceImpl(transportWebClient);
    }

    private WebClient.RequestHeadersUriSpec mockUriSpec;
    private WebClient.ResponseSpec mockResponseSpec;

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void mockSuccessResponse(Map<?, ?> responseMap) {
        mockUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        mockResponseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(Function.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));
    }

    @Test
    void shouldReturnMappedStationsWhenApiReturnsValidData() {
        Map<String, Object> station = new HashMap<>();
        station.put("id", "008503000");
        station.put("name", "Zurich HB");
        station.put("coordinate", Map.of("x", 8.540192, "y", 47.378177));

        mockSuccessResponse(Map.of("stations", List.of(station)));

        List<StationResponseDTO> result = stationService.buscarEstaciones("Zurich");

        assertEquals(1, result.size());
        assertEquals("008503000", result.get(0).getId());
        assertEquals("Zurich HB", result.get(0).getNombre());
        assertEquals(47.378177, result.get(0).getLatitud());
        assertEquals(8.540192, result.get(0).getLongitud());
    }

    @Test
    void shouldThrowNotFoundWhenItemsHaveNoId() {
        Map<String, Object> item = new HashMap<>();
        item.put("id", null);
        item.put("name", "XYZ Construction Sarl");

        mockSuccessResponse(Map.of("stations", List.of(item)));

        assertThrows(ResourceNotFoundException.class,
                () -> stationService.buscarEstaciones("xyz"));
    }

    @Test
    void shouldThrowNotFoundWhenStationsListIsEmpty() {
        mockSuccessResponse(Map.of("stations", List.of()));

        assertThrows(ResourceNotFoundException.class,
                () -> stationService.buscarEstaciones("xyz"));
    }

    @Test
    void shouldThrowNotFoundWhenStationsIsNull() {
        Map<String, Object> response = new HashMap<>();
        response.put("stations", null);
        mockSuccessResponse(response);

        assertThrows(ResourceNotFoundException.class,
                () -> stationService.buscarEstaciones("xyz"));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void shouldThrowServiceUnavailableWhenWebClientFails() {
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(transportWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(any(Function.class))).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("Connection refused")));

        TransportApiException ex = assertThrows(TransportApiException.class,
                () -> stationService.buscarEstaciones("Zurich"));
        assertEquals(503, ex.getStatus());
    }

    @Test
    void shouldReturnMultipleStations() {
        Map<String, Object> s1 = new HashMap<>();
        s1.put("id", "8503000");
        s1.put("name", "Zurich HB");
        s1.put("coordinate", Map.of("x", 8.540, "y", 47.378));

        Map<String, Object> s2 = new HashMap<>();
        s2.put("id", "8503010");
        s2.put("name", "Zurich Stadelhofen");

        mockSuccessResponse(Map.of("stations", List.of(s1, s2)));

        List<StationResponseDTO> result = stationService.buscarEstaciones("Zurich");

        assertEquals(2, result.size());
        assertEquals("Zurich HB", result.get(0).getNombre());
        assertEquals("Zurich Stadelhofen", result.get(1).getNombre());
    }
}
