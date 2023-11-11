package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.repository.AuthorRepository;
import com.nikolar.snippetbackend.response.AuthorResponse;
import com.nikolar.snippetbackend.response.SnippetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class QueryService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(1);
    @Autowired
    private WebClient searchService;
    @Autowired
    WebClient classificationService;
    @Autowired
    AuthorService authorService;


    public ResponseEntity<List<SnippetResponse>> query(String author, String book, String snippet){
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
    }

    public ResponseEntity<AuthorResponse> predict(String snippet){
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
    }

    public ResponseEntity<List<SnippetResponse>> aidedSearch(String snippet){
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
    }
}
