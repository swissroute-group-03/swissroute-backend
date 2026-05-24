package com.swissroute.config;

import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Value("${transport.api.base-url}")
    private String transportApiBaseUrl;

    @Bean
    public WebClient transportWebClient() {
        return WebClient.builder()
                .baseUrl(transportApiBaseUrl)
                .filter(errorHandlingFilter())
                .build();
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                HttpStatus status = HttpStatus.valueOf(response.statusCode().value());
                return response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new TransportApiException(
                                status.value(),
                                status.getReasonPhrase(),
                                body
                        )));
            }
            return Mono.just(response);
        });
    }
}