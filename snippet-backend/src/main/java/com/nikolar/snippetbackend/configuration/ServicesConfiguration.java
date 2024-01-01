package com.nikolar.snippetbackend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class ServicesConfiguration {

    @Value("${service.parser.address}")
    private String PARSER_SERVICE_ADDRESS;
    @Value("${service.search.address}")
    private String SEARCH_SERVICE_ADDRESS ;
    @Value("${service.classification.address}")
    private String CLASSIFICATION_SERVICE_ADDRESS;
    @Value("${service.summary.address}")
    private String SUMMARY_SERVICE_ADDRESS;
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
    @Bean
    public WebClient summaryService() {
        return WebClient.create(SUMMARY_SERVICE_ADDRESS);
    }
}
