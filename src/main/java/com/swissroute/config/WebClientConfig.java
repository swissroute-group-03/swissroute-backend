package com.swissroute.config;

import com.swissroute.exceptionHandler.exceptions.TransportApiException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${transport.api.base-url}")
    private String transportApiBaseUrl;

    @Bean
    public WebClient transportWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(transportApiBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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