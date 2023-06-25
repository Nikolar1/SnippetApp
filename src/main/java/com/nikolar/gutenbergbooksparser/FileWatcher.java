package com.nikolar.gutenbergbooksparser;

import com.nikolar.snippetbackend.lucene.IndexerThread;
import com.nikolar.snippetbackend.lucene.LuceneConfig;
import com.nikolar.snippetbackend.lucene.LuceneWriter;
import org.apache.logging.log4j.util.Timer;

import java.io.IOException;
import java.sql.Time;

public class FileWatcher {
    private static  FileWatcher instance;
    private long startTime = System.currentTimeMillis();
    private boolean testFileCreated;
    private boolean trainingFileCreated;
    private String testFileName;
    private String trainingFileName;
    private  boolean snippetsIndexed;
    private FileWatcher(){
        testFileCreated = false;
        trainingFileCreated = false;
    }

    public static synchronized FileWatcher getInstance(){
        if (instance == null){
            instance = new FileWatcher();
        }
        return instance;
    }

    public synchronized void setTestFileCreated(String testFileName) {
        this.testFileCreated = true;
        this.testFileName = testFileName;
        System.out.println("Test arff file created in " + ((((System.currentTimeMillis() - startTime))*1.0)/1000) + " seconds");
        createdFiles();
    }

    public synchronized void setTrainingFileCreated(String trainingFileName) {
        this.trainingFileCreated = true;
        this.trainingFileName = trainingFileName;
        System.out.println("Training arff file created in " + (((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
        createdFiles();
    }


    public synchronized void createdFiles(){
        if (areFilesCreated()){
            //Start indexing
            LuceneConfig lc = LuceneConfig.getInstance();
            lc.addFilePath("./" + trainingFileName);
            lc.addFilePath("./" + testFileName);
            try {
                LuceneWriter lcw = new LuceneWriter();
                lcw.start();
            }catch (IOException e){
                System.out.println("Error when trying to index snippets");
                e.printStackTrace();
            }
            //Start training and evaluation process(tbd)
        }
    }

    public void printTime(){
        System.out.println((((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public synchronized void setSnippetsIndexed() {
        this.snippetsIndexed = true;
        System.out.println("Snippets have been indexed in " + (((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public boolean areFilesCreated(){
        return testFileCreated && trainingFileCreated;
    }
    public boolean areSnippetsIndexed(){ return snippetsIndexed; }
}
