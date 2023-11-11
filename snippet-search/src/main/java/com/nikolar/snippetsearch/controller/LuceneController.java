package com.nikolar.snippetsearch.controller;

import com.nikolar.snippetsearch.response.SnippetResponse;
import com.nikolar.snippetsearch.service.LuceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/lucene/")
public class LuceneController {
    @Autowired
    LuceneService luceneService;
    @GetMapping("indexSnippets")
    public ResponseEntity<String> indexSnippets(){
        boolean snippetsIndexed = luceneService.indexSnippets();
        return new ResponseEntity<String>(snippetsIndexed ? "Snippets indexed" : "Failed", snippetsIndexed? HttpStatus.OK: HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/search")
    public ResponseEntity<List<SnippetResponse>> search(@RequestParam(value = "author", required=false) String author, @RequestParam(value = "book", required=false) String book, @RequestParam(value = "snippet", required=false) String snippet){
        //If the files aren't created yet send service unavailable indicating a temporary overload
        if (!luceneService.snippetsIndexed()){
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }

        List<SnippetResponse> rez = luceneService.query(author, book, snippet);
        return ResponseEntity.ok(rez);
    }

}
