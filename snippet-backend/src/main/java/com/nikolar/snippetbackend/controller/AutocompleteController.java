package com.nikolar.snippetbackend.controller;

import com.nikolar.snippetbackend.response.SnippetResponse;
import com.nikolar.snippetbackend.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AutocompleteController {
    @Autowired
    QueryService queryService;

    @MessageMapping("/autocomplete")
    @SendTo("/topic/autocomplete")
    public String autocomplete(String snippet) throws Exception {
        ResponseEntity<List<SnippetResponse>> response = queryService.query(null,null,snippet);
        if (response != null && response.getStatusCode().equals(HttpStatus.OK) && response.getBody()!=null && !response.getBody().isEmpty()) {
            String res = response.getBody().get(0).getSnippet();
            return res;
        }
        return "";
    }

    @MessageMapping("/aidedAutocomplete")
    @SendTo("/topic/aidedAutocomplete")
    public String aidedAutocomplete(String snippet) throws Exception {
        ResponseEntity<List<SnippetResponse>> response =  queryService.aidedSearch(snippet);
        if (response != null && response.getStatusCode().equals(HttpStatus.OK) && response.getBody()!=null && !response.getBody().isEmpty()) {
            return response.getBody().get(0).getSnippet();
        }
        return "";
    }
}
