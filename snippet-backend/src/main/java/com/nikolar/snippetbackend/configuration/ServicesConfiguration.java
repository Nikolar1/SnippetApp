package com.nikolar.snippetbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class ServicesConfiguration {
    private static final String PARSER_SERVICE_ADDRESS = "http://parser:8083/";
    private static final String SEARCH_SERVICE_ADDRESS = "http://search:8082/";
    private static final String CLASSIFICATION_SERVICE_ADDRESS = "http://classification:8081/";
    @Bean
    public WebClient parserService() {
        return WebClient.create(PARSER_SERVICE_ADDRESS);
    }

    @Bean
    public WebClient searchService() {
        return WebClient.create(SEARCH_SERVICE_ADDRESS);
    }

    @Bean
    public WebClient classificationService() {
        return WebClient.create(CLASSIFICATION_SERVICE_ADDRESS);
    }
}
