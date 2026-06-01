package com.swissroute.integration.estacionesfavoritas;

import com.swissroute.dto.request.FavoriteStationRequestDTO;
import com.swissroute.dto.response.FavoriteStationResponseDTO;
import com.swissroute.integration.BaseIntegrationTest;
import com.swissroute.service.use_cases.FavoriteStationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteStationIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private FavoriteStationService favoriteStationService;

    @Test
    void shouldCreateAndListFavoriteStations() throws Exception {
        String token = registerAndLogin();

        FavoriteStationResponseDTO responseDTO = new FavoriteStationResponseDTO(1L, "8503000", "Zurich HB");

        when(favoriteStationService.createFavoriteStation(any(FavoriteStationRequestDTO.class)))
                .thenReturn(responseDTO);
        when(favoriteStationService.getFavoriteStations())
                .thenReturn(List.of(responseDTO));

        String body = """
                {
                  "externalStationId": "8503000",
                  "stationName": "Zurich HB"
                }
                """;

        // Create
        mockMvc.perform(post("/api/estaciones-favoritas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationName").value("Zurich HB"));

        // List
        mockMvc.perform(get("/api/estaciones-favoritas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].stationName").value("Zurich HB"));
    }
}
