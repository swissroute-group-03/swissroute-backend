package com.swissroute.integration.transporte;

import com.swissroute.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransporteIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnStationsWhenQueryIsValid() throws Exception {
        String token = registerAndLogin();

        mockMvc.perform(get("/api/estaciones")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("query", "Zurich")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").exists());
    }

    @Test
    void shouldReturn404WhenNoStationFound() throws Exception {
        String token = registerAndLogin();

        mockMvc.perform(get("/api/estaciones")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("query", "StationThatDoesNotExist12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConnectionsWhenOriginAndDestinationAreValid() throws Exception {
        String token = registerAndLogin();

        mockMvc.perform(get("/api/conexiones")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("from", "Lausanne")
                        .param("to", "Geneva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].origen").value("Lausanne"))
                .andExpect(jsonPath("$[0].destino").value("Genève"));
    }

    @Test
    void shouldReturnStationboardWhenStationIsValid() throws Exception {
        String token = registerAndLogin();

        mockMvc.perform(get("/api/tablon")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("station", "Bern")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].serviceName").exists());
    }

    @Test
    void shouldReturn401WhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/estaciones")
                        .param("query", "Zurich"))
                .andExpect(status().isUnauthorized());
    }
}
