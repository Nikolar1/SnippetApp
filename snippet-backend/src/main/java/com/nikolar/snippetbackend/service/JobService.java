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
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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
        pingService(parserService, ServiceType.PARSER);
        pingService(searchService, ServiceType.SEARCH);
        pingService(classificationService, ServiceType.CLASSIFICATION);
    }

    private boolean pingService(WebClient service, ServiceType serviceType) {
        ServiceStatusDto oldServiceStatus = serviceStatusService.getLatestServiceStatus();
        if(oldServiceStatus == null){
            oldServiceStatus = new ServiceStatusDto(null,null,Status.DOWNED,Status.DOWNED,Status.DOWNED);
        }
        return pingService(service, serviceType, oldServiceStatus);
    }

    private boolean pingService(WebClient service, ServiceType serviceType, ServiceStatusDto oldServiceStatus){
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
        ServiceStatusDto newServiceStatus = new ServiceStatusDto();
        boolean isRunning = response.getStatusCode().equals(HttpStatus.OK);
        switch (serviceType){
            case PARSER:
                newServiceStatus.setParserService(isRunning? Status.RUNNING : Status.DOWNED);
                newServiceStatus.setSearchService(oldServiceStatus.getSearchService());
                newServiceStatus.setClassificationService(oldServiceStatus.getClassificationService());
                break;
            case SEARCH:
                newServiceStatus.setParserService(oldServiceStatus.getParserService());
                newServiceStatus.setSearchService(isRunning? Status.RUNNING : Status.DOWNED);
                newServiceStatus.setClassificationService(oldServiceStatus.getClassificationService());
                break;
            case CLASSIFICATION:
                newServiceStatus.setParserService(oldServiceStatus.getParserService());
                newServiceStatus.setSearchService(oldServiceStatus.getSearchService());
                newServiceStatus.setClassificationService(isRunning? Status.RUNNING : Status.DOWNED);
                break;
        }
        newServiceStatus.setDateTime(LocalDateTime.now());
        if (!oldServiceStatus.equalServicesStatus(newServiceStatus)){
            serviceStatusService.saveServiceStatus(newServiceStatus);
        }
        return isRunning;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void startOtherServices() {
        CompletableFuture.runAsync(() -> {
            while (!pingService(parserService, ServiceType.PARSER)) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            // Perform the integrity check after the parser is ready
            performIntegrityCheck();

            while (!pingService(searchService, ServiceType.SEARCH) && !pingService(classificationService, ServiceType.CLASSIFICATION)) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            // Start indexing and classifying after both search and classification services are ready
            startIndexingAndClassifying();
        });
    }

    private void performIntegrityCheck() {
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

    private void startIndexingAndClassifying() {
        System.out.println("Started indexing and classifying");

        CompletableFuture<Void> indexingFuture = searchService
                .get()
                .uri("/lucene/indexSnippets")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .subscribeOn(Schedulers.single())
                .toFuture()
                .thenAccept(result -> {
                    // Handle the result if needed
                });

        CompletableFuture<Void> classificationFuture = classificationService
                .get()
                .uri("/classification/train")
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .subscribeOn(Schedulers.single())
                .toFuture()
                .thenAccept(result -> {
                    // Handle the result if needed
                });

        CompletableFuture.allOf(indexingFuture, classificationFuture).join();
    }
}
