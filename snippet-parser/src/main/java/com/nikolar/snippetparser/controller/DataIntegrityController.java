package com.nikolar.snippetparser.controller;

import com.nikolar.snippetparser.service.DataIntegrityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/integrity/")
public class DataIntegrityController {
    @Autowired
    DataIntegrityService dataIntegrityService;
    @GetMapping("checkDataIntegrity")
    public ResponseEntity<String> checkDataIntegrity(){
        dataIntegrityService.checkDataIntegrity();
        return new ResponseEntity<>("Checked", HttpStatus.OK);
    }
}
