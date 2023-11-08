package com.nikolar.snippetbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class ServicesConfiguration {
    @Bean
    public WebClient parserService() {
        return WebClient.create("http://localhost:8083/");
    }
}
