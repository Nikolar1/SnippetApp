package com.nikolar.snippetbackend.controller;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import com.nikolar.snippetbackend.lucene.QueryProccesor;
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
public class LuceneController {
    @Autowired
    private QueryProccesor queryProccesor;

    @GetMapping("/search")
    public ResponseEntity index(@RequestParam("query") String queryString){
        FileWatcher fl = FileWatcher.getInstance();
        //If the files aren't created yet send service unavailable indicating a temporary overload
        if (!fl.areFilesCreated()){
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
        List rez = queryProccesor.query(queryString);
        return ResponseEntity.ok(rez);
    }
}
