package com.nikolar.snippetbackend.controller;

import com.nikolar.snippetbackend.request.SummaryRequest;
import com.nikolar.snippetbackend.response.StatusResponse;
import com.nikolar.snippetbackend.response.SummaryResponse;
import com.nikolar.snippetbackend.service.JobService;
import com.nikolar.snippetbackend.service.QueryService;
import com.nikolar.snippetbackend.service.SanitazationService;
import com.nikolar.snippetbackend.service.ServiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST})
public class LuceneController {
    @Autowired
    private QueryService queryService;
    @Autowired
    private SanitazationService sanitazationService;
    @Autowired
    private ServiceStatusService serviceStatusService;
    @Autowired
    private JobService jobService;

    @GetMapping("/serviceStatus")
    public  ResponseEntity<StatusResponse> serviceStatus(){
        return new ResponseEntity<>(jobService.checkServiceStatus(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "author", required=false) String author, @RequestParam(value = "book", required=false) String book, @RequestParam(value = "snippet", required=false) String snippet){
        String sanitizedAuthor = sanitazationService.sanitizeQueryParam(author);
        String sanitizedBook = sanitazationService.sanitizeQueryParam(book);
        String sanitizedSnippet = sanitazationService.sanitizeQueryParam(snippet);

        return queryService.query(sanitizedAuthor, sanitizedBook, sanitizedSnippet);
    }

    @PostMapping("/summarize")
    public ResponseEntity<?> summarize(@RequestBody SummaryRequest summaryRequest){
        SummaryResponse response = queryService.sumarize(summaryRequest).getBody();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/aidedSearch")
    public ResponseEntity<?> aidedSearch(@RequestParam(value = "snippet", required=false) String snippet){
        String sanitizedSnippet = sanitazationService.sanitizeQueryParam(snippet);
        return queryService.aidedSearch(sanitizedSnippet);
    }
    @GetMapping("/predict")
    public ResponseEntity<?> predict(@RequestParam(value = "snippet", required=false) String snippet){
        String sanitizedSnippet = sanitazationService.sanitizeQueryParam(snippet);
        return queryService.predict(sanitizedSnippet);
    }
}
