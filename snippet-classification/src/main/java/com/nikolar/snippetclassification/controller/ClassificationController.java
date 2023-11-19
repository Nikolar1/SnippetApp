package com.nikolar.snippetclassification.controller;

import com.nikolar.snippetclassification.response.AuthorResponse;
import com.nikolar.snippetclassification.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/classification/")
public class ClassificationController {
    @Autowired
    ClassificationService classificationService;
    @GetMapping("train")
    public ResponseEntity<String> indexSnippets(){
        boolean isTrained = classificationService.train();
        return new ResponseEntity<String>(isTrained ? "Training complete" : "Failed", isTrained? HttpStatus.OK: HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/predict")
    public ResponseEntity<AuthorResponse> predict(@RequestParam(value = "snippet", required=false) String snippet){
        //If classification isn't finished yet send service unavailable indicating a temporary overload
        if (!classificationService.isReady()){
            return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
        }
        AuthorResponse rez = classificationService.predict(snippet);
        return ResponseEntity.ok(rez);
    }
}
