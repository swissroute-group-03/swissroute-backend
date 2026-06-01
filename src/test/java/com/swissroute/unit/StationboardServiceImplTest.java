package com.swissroute.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.swissroute.dto.response.StationboardDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.service.StationboardServiceImpl;

import reactor.core.publisher.Mono;

class StationboardServiceImplTest {

    private WebClient transportWebClient;
    private StationboardServiceImpl stationboardService;

    @BeforeEach
    void setUp() {
        transportWebClient = mock(WebClient.class);
        stationboardService = new StationboardServiceImpl(transportWebClient);
    }

    private WebClient.RequestHeadersUriSpec mockUriSpec;
    private WebClient.ResponseSpec mockResponseSpec;

    @SuppressWarnings("unchecked")
    private void mockWebClientGet(Map<String, Object> response) {
        mockUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        mockResponseSpec = mock(WebClient.ResponseSpec.class);
        when(transportWebClient.get()).thenReturn(mockUriSpec);
        when(mockUriSpec.uri(any(Function.class))).thenReturn(mockUriSpec);
        when(mockUriSpec.retrieve()).thenReturn(mockResponseSpec);
        when(mockResponseSpec.onStatus(any(), any())).thenReturn(mockResponseSpec);
        when(mockResponseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(response));
    }

    @Test
    void getStationboard_ShouldReturnList_WhenValidStation() {
        Map<String, Object> item = Map.of(
                "name", "S 41",
                "category", "S",
                "to", "Bern",
                "stop", Map.of("departure", "2026-06-01T17:00:00+02:00")
        );
        mockWebClientGet(Map.of("stationboard", List.of(item)));

        List<StationboardDTO> result = stationboardService.getStationboard("Lausanne", 10, Optional.empty());

        assertEquals(1, result.size());
        assertEquals("S 41", result.get(0).serviceName());
        assertEquals("Bern", result.get(0).finalDestination());
        assertEquals(LocalDateTime.of(2026, 6, 1, 17, 0), result.get(0).departureTime());
    }

    @Test
    void getStationboard_ShouldThrowNotFound_WhenNoItemsFound() {
        mockWebClientGet(Map.of("stationboard", List.of()));

        assertThrows(ResourceNotFoundException.class, 
                () -> stationboardService.getStationboard("X", 10, Optional.empty()));
    }

    @Test
    void getStationboard_ShouldFilterItemsWithoutName() {
        Map<String, Object> valid = Map.of("name", "S 1", "to", "X", "stop", Map.of());
        Map<String, Object> invalid = Map.of("to", "Y");
        
        mockWebClientGet(Map.of("stationboard", List.of(valid, invalid)));

        List<StationboardDTO> result = stationboardService.getStationboard("L", 10, Optional.empty());

        assertEquals(1, result.size());
        assertEquals("S 1", result.get(0).serviceName());
    }
}
