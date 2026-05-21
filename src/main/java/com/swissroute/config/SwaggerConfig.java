package com.swissroute.config;

import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "SwissRoute API",
                description = "Documentacion de los servicios REST del sistema SwissRoute para planificacion y seguimiento de viajes en transporte publico.",
                termsOfService = "http://localhost:8080/terminos-y-servicios",
                contact = @Contact(
                        name = "SwissRoute Group 03",
                        email = "support-group03@swissroute.com"
                ),
                version = "1.0.0",
                summary = "Documentacion APIs SwissRoute",
                license = @License(
                        name = "GROUP 03",
                        identifier = "G03",
                        url = "https://github.com/swissroute-group-03/swissroute-backend"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor Local",
                        url = "http://localhost:8080"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Token JWT para autorizar endpoints protegidos",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}