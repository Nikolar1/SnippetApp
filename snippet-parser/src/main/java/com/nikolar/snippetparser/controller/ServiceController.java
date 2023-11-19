package com.nikolar.snippetparser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ServiceController {
    @GetMapping("ping")
    public ResponseEntity<String> ping(){
        return new ResponseEntity<>("Running", HttpStatus.OK);
    }
}
