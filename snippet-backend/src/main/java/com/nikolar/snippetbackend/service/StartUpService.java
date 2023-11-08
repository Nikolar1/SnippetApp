package com.nikolar.snippetbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class StartUpService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(10);
    private static final Duration PING_REQUEST_TIMEOUT = Duration.ofSeconds(5);
    @Autowired
    private WebClient parserService;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void startOtherServices() {
        ResponseEntity<String> response;
        do {
            try {
                response = parserService
                        .get()
                        .uri("/ping")
                        .retrieve()
                        .toEntity(String.class)
                        .onErrorStop()
                        .block(REQUEST_TIMEOUT);
            }catch (Exception e){
                response = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }
        }while (response.getStatusCode() != HttpStatus.OK);
        System.out.println("Parser running starting integrity check");
        do {
            response = parserService
                    .get()
                    .uri("/integrity/checkDataIntegrity")
                    .retrieve()
                    .toEntity(String.class)
                    .onErrorStop()
                    .block(REQUEST_TIMEOUT);
        }while (response.getStatusCode() != HttpStatus.OK);
        System.out.println("Finished integrity check");
    }
}
