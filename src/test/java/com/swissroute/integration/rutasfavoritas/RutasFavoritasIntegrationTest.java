package com.swissroute.integration.rutasfavoritas;

import com.swissroute.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RutasFavoritasIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturn400WhenFavoriteRouteNameIsEmpty() throws Exception {
        String token = registerAndLogin();

        String body = """
                {
                  "nombre": "",
                  "origenId": "8507000",
                  "origenNombre": "Bern",
                  "destinoId": "8503000",
                  "destinoNombre": "Zurich",
                  "tipoTransporte": "train"
                }
                """;

        mockMvc.perform(post("/api/rutas-favoritas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenFavoriteRouteNameAlreadyExistsForSameUser() throws Exception {
        String token = registerAndLogin();

        String nombreRuta = "Ruta duplicada " + UUID.randomUUID();

        String body = """
                {
                  "nombre": "%s",
                  "origenId": "8507000",
                  "origenNombre": "Bern",
                  "destinoId": "8503000",
                  "destinoNombre": "Zurich",
                  "tipoTransporte": "train"
                }
                """.formatted(nombreRuta);

        mockMvc.perform(post("/api/rutas-favoritas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/rutas-favoritas")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }
}
