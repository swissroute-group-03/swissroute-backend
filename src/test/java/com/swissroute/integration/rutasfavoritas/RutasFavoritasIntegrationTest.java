package com.swissroute.integration.rutasfavoritas;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional
class RutasFavoritasIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

    private String registerAndLogin() throws Exception {
        String email = "test_" + UUID.randomUUID() + "@example.com";
        String password = "Password123!";

        String registerJson = """
                {
                  "email": "%s",
                  "password": "%s",
                  "name": "Usuario Test",
                  "ciudadBase": "Zurich"
                }
                """.formatted(email, password);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        MvcResult result = mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.jwt");
    }
}