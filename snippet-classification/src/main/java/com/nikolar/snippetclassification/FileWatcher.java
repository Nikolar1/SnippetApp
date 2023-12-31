package com.nikolar.snippetclassification;

import com.nikolar.snippetclassification.learning.LearningService;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileWatcher {
    private static  FileWatcher instance;
    private final long startTime = System.currentTimeMillis();
    private boolean trainingComplete;
    private boolean classifierReady;
    @Getter
    @Setter
    private String testFileName;



    private FileWatcher(){
        trainingComplete = false;
        classifierReady = false;
    }

    public static synchronized FileWatcher getInstance(){
        if (instance == null){
            instance = new FileWatcher();
        }
        return instance;
    }


    public void printTime(){
        System.out.println((((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public void printMessageWithTime( String message ){
        System.out.println( message + " in " + ( ( ( System.currentTimeMillis() - startTime ) * 1.0 ) / 1000 ) + " seconds" );
    }

    public void setClassifierReady(){
        this.classifierReady = true;
    }

    public synchronized void setTrainingComplete(){
        trainingComplete = true;
        System.out.println("Training is complete in " + (((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");

        LearningService learningService = SpringContext.getBean(LearningService.class);
        learningService.loadClassifier();
    }


    public boolean isTrainingComplete(){
        return trainingComplete;
    }
    public boolean isClassifierReady(){ return classifierReady; }
}
