package com.nikolar.snippetparser.service;

import com.nikolar.snippetparser.gutenbergbooksparser.DataConverter;
import com.nikolar.snippetparser.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataIntegrityService {
    @Autowired
    DataConverter dataConverter;
    @Autowired
    SnippetRepository snippetRepository;
    public void checkDataIntegrity(){
        if (snippetRepository.count() < 194000) {
            dataConverter.saveToDatabaseTraining();
            dataConverter.saveToDatabaseTest();
        }
    }
}
