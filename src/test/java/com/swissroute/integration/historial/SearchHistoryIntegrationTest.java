package com.swissroute.integration.historial;

import com.swissroute.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchHistoryIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldRegisterAndRetrieveHistory() throws Exception {
        String token = registerAndLogin();

        // 1. Initial history should be empty
        mockMvc.perform(get("/api/historial")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        // 2. Perform a search to trigger history registration
        mockMvc.perform(get("/api/conexiones")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("from", "Lausanne")
                        .param("to", "Geneva"))
                .andExpect(status().isOk());

        // 3. History should now have one entry
        mockMvc.perform(get("/api/historial")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].origen").value("Lausanne"))
                .andExpect(jsonPath("$.content[0].destino").value("Geneva"));
    }

    @Test
    void shouldDeleteAllHistory() throws Exception {
        String token = registerAndLogin();

        // Perform search
        mockMvc.perform(get("/api/conexiones")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("from", "Bern")
                        .param("to", "Zurich"))
                .andExpect(status().isOk());

        // Delete all
        mockMvc.perform(delete("/api/historial")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent());

        // Verify empty
        mockMvc.perform(get("/api/historial")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}
