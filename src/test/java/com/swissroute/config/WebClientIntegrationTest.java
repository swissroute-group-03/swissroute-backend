package com.swissroute.config;

import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@SpringBootTest(
        classes = WebClientConfig.class,
        properties = {
                "transport.api.base-url=http://transport.opendata.ch/v1",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration"
        }
)
class WebClientIntegrationTest {

    @Autowired
    @Qualifier("transportWebClient")
    private WebClient transportWebClient;

    @Test
    void shouldReturn200WhenQueryBasel() {
        var response = transportWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/locations")
                        .queryParam("query", "Basel")
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        StepVerifier.create(response)
                .expectNextMatches(body -> body.contains("Basel"))
                .verifyComplete();
    }

    @Test
    void shouldThrowTransportApiExceptionOnNonExistentPath() {
        var response = transportWebClient.get()
                .uri("/nonexistent")
                .retrieve()
                .bodyToMono(String.class);

        StepVerifier.create(response)
                .expectError(TransportApiException.class)
                .verify();
    }
}
