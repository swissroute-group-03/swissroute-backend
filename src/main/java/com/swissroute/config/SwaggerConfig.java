package com.swissroute.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Security Token";

        return new OpenAPI()
                .info(new Info()
                        .title("SwissRoute API")
                        .version("1.0.0")
                        .description("Documentación de los servicios REST del sistema SwissRoute para planificación y seguimiento de viajes en transporte público.\n\n"
                                + "**Funcionalidades principales:**\n"
                                + "- **Búsqueda de estaciones:** Por nombre o coordenadas geográficas.\n"
                                + "- **Conexiones:** Consulta de rutas entre estaciones con filtros.\n"
                                + "- **Tablón de salidas:** Salidas programadas en estaciones.\n"
                                + "- **Rutas favoritas:** Gestión de rutas favoritas del usuario (persistencia local y proxy externo).\n"
                                + "- **Estaciones favoritas:** CRUD de estaciones favoritas vía API externa.\n"
                                + "- **Autenticación:** Login y registro con JWT.\n\n"
                                + "Desarrollado por [José Saire](https://github.com/msxd26), "
                                + "[Alexandra](https://github.com/Alexandra9804) y "
                                + "[Diógenes Quintero](https://github.com/dio-quincarDev) "
                                + "como reto para la comunidad **BytesColaborativos**.")
                        .contact(new Contact()
                                .name("José Saire, Alexandra Chavez y Diógenes Quintero")
                                .url("https://github.com/swissroute-group-03/swissroute-backend"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Token JWT para autorizar endpoints protegidos.\n"
                                                + "Formato: `Bearer <your-token>`")));
    }
}
