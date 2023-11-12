package com.nikolar.snippetparser.service;

import com.nikolar.snippetparser.gutenbergbooksparser.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataIntegrityService {
    @Autowired
    DataConverter dataConverter;
    public void checkDataIntegrity(){
        dataConverter.saveToDatabaseTraining();
        dataConverter.saveToDatabaseTest();
    }
}
