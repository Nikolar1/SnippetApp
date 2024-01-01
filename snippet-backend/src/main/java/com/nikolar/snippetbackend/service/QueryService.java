package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.repository.AuthorRepository;
import com.nikolar.snippetbackend.request.SummaryRequest;
import com.nikolar.snippetbackend.response.AuthorResponse;
import com.nikolar.snippetbackend.response.SnippetResponse;
import com.nikolar.snippetbackend.response.SnippetSummarizedResponse;
import com.nikolar.snippetbackend.response.SummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(1);
    @Autowired
    private WebClient searchService;
    @Autowired
    WebClient classificationService;
    @Autowired
    private WebClient summaryService;
    @Autowired
    AuthorService authorService;



    public ResponseEntity<SummaryResponse> sumarize(SummaryRequest summaryRequest){
        try {
            return summaryService.post()
                    .uri("/model/predict")
                    .bodyValue(summaryRequest)
                    .retrieve()
                    .toEntity(SummaryResponse.class)
                    .onErrorStop()
                    .block(REQUEST_TIMEOUT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<List<SnippetResponse>> query(String author, String book, String snippet){
        try {
            return searchService
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/lucene/search")
                            .queryParam("snippet", snippet)
                            .queryParam("book", book)
                            .queryParam("author", author)
                            .build())
                    .retrieve()
                    .toEntityList(SnippetResponse.class)
                    .onErrorStop()
                    .block(REQUEST_TIMEOUT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<AuthorResponse> predict(String snippet){
        try {
        return classificationService
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/classification/predict")
                        .queryParam("snippet", snippet)
                        .build())
                .retrieve()
                .toEntity(AuthorResponse.class)
                .onErrorStop()
                .block(REQUEST_TIMEOUT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<List<SnippetResponse>> aidedSearch(String snippet){
        try {
        ResponseEntity<AuthorResponse> authorResponse = predict(snippet);
        return searchService
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lucene/search")
                        .queryParam("snippet", snippet)
                        .queryParam("author", authorResponse.getBody().getAuthor())
                        .build())
                .retrieve()
                .toEntityList(SnippetResponse.class)
                .onErrorStop()
                .block(REQUEST_TIMEOUT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
