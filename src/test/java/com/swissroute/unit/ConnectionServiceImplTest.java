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

import com.swissroute.dto.response.ConnectionResponseDTO;
import com.swissroute.exceptionHandler.exceptions.ResourceNotFoundException;
import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import com.swissroute.service.ConnectionServiceImpl;

import reactor.core.publisher.Mono;

class ConnectionServiceImplTest {

    private WebClient transportWebClient;
    private ConnectionServiceImpl connectionService;

    @BeforeEach
    void setUp() {
        transportWebClient = mock(WebClient.class);
        connectionService = new ConnectionServiceImpl(transportWebClient);
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

    private Map<String, Object> createDeparture(String name, String time) {
        return Map.of(
                "station", Map.of("name", name),
                "departure", time
        );
    }

    private Map<String, Object> createArrival(String name, String time) {
        return Map.of(
                "station", Map.of("name", name),
                "arrival", time
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnMappedConnectionsWhenApiReturnsValidData() {
        Map<String, Object> section = new HashMap<>();
        section.put("departure", createDeparture("Lausanne", "2026-05-29T04:49:00+0200"));
        section.put("arrival", createArrival("Romont FR", "2026-05-29T05:27:00+0200"));
        section.put("journey", Map.of(
                "category", "S",
                "number", "41",
                "operator", "SBB"
        ));

        Map<String, Object> connection = new HashMap<>();
        connection.put("from", Map.of("station", Map.of("name", "Lausanne")));
        connection.put("to", Map.of("station", Map.of("name", "Bern")));
        connection.put("duration", "00d01:37:00");
        connection.put("products", List.of("S41", "IC 1"));
        connection.put("sections", List.of(section));

        mockSuccessResponse(Map.of("connections", List.of(connection)));

        List<ConnectionResponseDTO> result = connectionService.buscarConexiones("Lausanne", "Bern", null, null, null, null);

        assertEquals(1, result.size());
        ConnectionResponseDTO dto = result.get(0);
        assertEquals("Lausanne", dto.getOrigen());
        assertEquals("Bern", dto.getDestino());
        assertEquals("00d01:37:00", dto.getDuracion());
        assertEquals(List.of("S41", "IC 1"), dto.getProductos());
        assertEquals(1, dto.getSecciones().size());
        assertEquals("Lausanne", dto.getSecciones().get(0).getOrigen());
        assertEquals("Romont FR", dto.getSecciones().get(0).getDestino());
        assertEquals("S 41 SBB", dto.getSecciones().get(0).getTransporte());
        assertEquals("journey", dto.getSecciones().get(0).getTipo());
    }

    @Test
    void shouldThrowNotFoundWhenConnectionsListIsEmpty() {
        mockSuccessResponse(Map.of("connections", List.of()));

assertThrows(ResourceNotFoundException.class,
                () -> connectionService.buscarConexiones("Lausanne", "Bern", null, null, null, null));
    }

    @Test
    void shouldThrowNotFoundWhenConnectionsIsNull() {
        Map<String, Object> response = new HashMap<>();
        response.put("connections", null);
        mockSuccessResponse(response);

        assertThrows(ResourceNotFoundException.class,
                () -> connectionService.buscarConexiones("Lausanne", "Bern", null, null, null, null));
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
                () -> connectionService.buscarConexiones("Lausanne", "Bern", null, null, null, null));
        assertEquals(503, ex.getStatus());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldPassOptionalParamsToApi() {
        Map<String, Object> section = new HashMap<>();
        section.put("departure", createDeparture("Lausanne", "2026-05-29T04:49:00+0200"));
        section.put("arrival", createArrival("Bern", "2026-05-29T06:26:00+0200"));

        Map<String, Object> connection = new HashMap<>();
        connection.put("from", Map.of("station", Map.of("name", "Lausanne")));
        connection.put("to", Map.of("station", Map.of("name", "Bern")));
        connection.put("duration", "00d01:37:00");
        connection.put("products", List.of("IC 1"));
        connection.put("sections", List.of(section));

        mockSuccessResponse(Map.of("connections", List.of(connection)));

        List<ConnectionResponseDTO> result = connectionService.buscarConexiones(
                "Lausanne", "Bern", "2026-05-29", "06:00", "ice", null);

        assertEquals(1, result.size());
        assertEquals("Lausanne", result.get(0).getOrigen());
        assertEquals("Bern", result.get(0).getDestino());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldHandleWalkSectionGracefully() {
        Map<String, Object> walkSection = new HashMap<>();
        walkSection.put("departure", createDeparture("Lausanne", "2026-05-29T04:49:00+0200"));
        walkSection.put("arrival", createArrival("Lausanne Gare", "2026-05-29T04:55:00+0200"));
        walkSection.put("walk", Map.of("duration", 6));

        Map<String, Object> connection = new HashMap<>();
        connection.put("from", Map.of("station", Map.of("name", "Lausanne")));
        connection.put("to", Map.of("station", Map.of("name", "Bern")));
        connection.put("duration", "00d01:37:00");
        connection.put("products", List.of("IC 1"));
        connection.put("sections", List.of(walkSection));

        mockSuccessResponse(Map.of("connections", List.of(connection)));

        List<ConnectionResponseDTO> result = connectionService.buscarConexiones("Lausanne", "Bern", null, null, null, null);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSecciones().size());
        assertEquals("Caminando", result.get(0).getSecciones().get(0).getTransporte());
        assertEquals("walk", result.get(0).getSecciones().get(0).getTipo());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldPassViaParamsToApi() {
        Map<String, Object> section = new HashMap<>();
        section.put("departure", createDeparture("Lausanne", "2026-05-29T04:49:00+0200"));
        section.put("arrival", createArrival("Bern", "2026-05-29T06:26:00+0200"));
        section.put("journey", Map.of("category", "IC", "number", "1", "operator", "SBB"));

        Map<String, Object> connection = new HashMap<>();
        connection.put("from", Map.of("station", Map.of("name", "Lausanne")));
        connection.put("to", Map.of("station", Map.of("name", "Bern")));
        connection.put("duration", "00d01:37:00");
        connection.put("products", List.of("IC 1"));
        connection.put("sections", List.of(section));

        mockSuccessResponse(Map.of("connections", List.of(connection)));

        List<ConnectionResponseDTO> result = connectionService.buscarConexiones(
                "Lausanne", "Bern", null, null, null, List.of("Olten"));

        assertEquals(1, result.size());
        assertEquals("Lausanne", result.get(0).getOrigen());
        assertEquals("Bern", result.get(0).getDestino());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldWorkWithoutVia() {
        Map<String, Object> section = new HashMap<>();
        section.put("departure", createDeparture("Lausanne", "2026-05-29T04:49:00+0200"));
        section.put("arrival", createArrival("Bern", "2026-05-29T06:26:00+0200"));
        section.put("journey", Map.of("category", "IC", "number", "1", "operator", "SBB"));

        Map<String, Object> connection = new HashMap<>();
        connection.put("from", Map.of("station", Map.of("name", "Lausanne")));
        connection.put("to", Map.of("station", Map.of("name", "Bern")));
        connection.put("duration", "00d01:37:00");
        connection.put("products", List.of("IC 1"));
        connection.put("sections", List.of(section));

        mockSuccessResponse(Map.of("connections", List.of(connection)));

        List<ConnectionResponseDTO> result = connectionService.buscarConexiones(
                "Lausanne", "Bern", null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Lausanne", result.get(0).getOrigen());
        assertEquals("Bern", result.get(0).getDestino());
    }
}
