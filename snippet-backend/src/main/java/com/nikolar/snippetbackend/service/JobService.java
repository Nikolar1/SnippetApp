package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.ServiceStatusDto;
import com.nikolar.snippetbackend.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@EnableAsync
public class JobService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(10);
    private static final Duration PING_REQUEST_TIMEOUT = Duration.ofSeconds(5);
    @Autowired
    private WebClient parserService;
    @Autowired
    private WebClient searchService;
    @Autowired
    WebClient classificationService;
    @Autowired
    private ServiceStatusService serviceStatusService;

    @Scheduled(fixedDelay = 300000, initialDelay = 10000)
    public void checkServiceStatus(){
        ServiceStatusDto oldServiceStatus = serviceStatusService.getLatestServiceStatus();
        ServiceStatusDto newServiceStatus = new ServiceStatusDto();
        newServiceStatus.setParserService(pingService(parserService)? Status.RUNNING : Status.DOWNED);
        newServiceStatus.setParserService(pingService(searchService)? Status.RUNNING : Status.DOWNED);
        newServiceStatus.setParserService(pingService(classificationService)? Status.RUNNING : Status.DOWNED);
        newServiceStatus.setDateTime(LocalDateTime.now());
        if (oldServiceStatus.equalServicesStatus(newServiceStatus)){
            serviceStatusService.saveServiceStatus(newServiceStatus);
        }

    }

    private boolean pingService(WebClient service){
        ResponseEntity<String> response;
        try {
            response = service
                    .get()
                    .uri("/ping")
                    .retrieve()
                    .toEntity(String.class)
                    .onErrorStop()
                    .block(PING_REQUEST_TIMEOUT);
        }catch (Exception e){
            response = new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void startOtherServices() {
        while (!pingService(parserService)){
            try {
                wait(5000);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        ResponseEntity<String> response;
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
