package com.nikolar.snippetbackend.controller;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.lucene.QueryProccesor;
import com.nikolar.snippetbackend.service.SanitazationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Component
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LuceneController {
    @Autowired
    private QueryProccesor queryProccesor;
    @Autowired
    private SanitazationService sanitazationService;

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam(value = "author", required=false) String author, @RequestParam(value = "book", required=false) String book, @RequestParam(value = "snippet", required=false) String snippet){
        FileWatcher fl = FileWatcher.getInstance();
        //If the files aren't created yet send service unavailable indicating a temporary overload
        if (!fl.areFilesCreated() || !fl.areSnippetsIndexed()){
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
        String sanitizedAuthor = sanitazationService.sanitizeQueryParam(author);
        String sanitizedBook = sanitazationService.sanitizeQueryParam(book);
        String sanitizedSnippet = sanitazationService.sanitizeQueryParam(snippet);

        List<SnippetDto> rez = queryProccesor.query(sanitizedAuthor, sanitizedBook, sanitizedSnippet);
        return ResponseEntity.ok(rez);
    }

    @GetMapping("/aidedSearch")
    public ResponseEntity aidedSearch(@RequestParam(value = "snippet", required=false) String snippet){
        FileWatcher fl = FileWatcher.getInstance();
        //If the files aren't created yet send service unavailable indicating a temporary overload
        if (!fl.areFilesCreated() || !fl.areSnippetsIndexed() || !fl.isTrainingComplete()){
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
        String sanitizedSnippet = sanitazationService.sanitizeQueryParam(snippet);

        List<SnippetDto> rez = queryProccesor.aidedQuery(sanitizedSnippet);
        return ResponseEntity.ok(rez);
    }
}
