package com.swissroute.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${transport.api.base-url}")
    private String transportApiBaseUrl;

    @Bean
    public WebClient transportWebClient() {
        return WebClient.builder()
                .baseUrl(transportApiBaseUrl)
                .build();
    }
}