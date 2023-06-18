package com.nikolar.gutenbergbooksparser;

import com.nikolar.snippetbackend.lucene.IndexerThread;
import com.nikolar.snippetbackend.lucene.LuceneConfig;

public class FileWatcher {
    private static  FileWatcher instance;
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
        System.out.println("Test arff file created");
        createdFiles();
    }

    public synchronized void setTrainingFileCreated(String trainingFileName) {
        this.trainingFileCreated = true;
        this.trainingFileName = trainingFileName;
        System.out.println("Training arff file created");
        createdFiles();
    }


    public synchronized void createdFiles(){
        if (areFilesCreated()){
            //Start indexing
            LuceneConfig lc = LuceneConfig.getInstance();
            lc.addFilePath("./" + trainingFileName);
            lc.addFilePath("./" + testFileName);
            IndexerThread it = new IndexerThread();
            it.start();
            //Start training and evaluation process(tbd)
        }
    }

    public synchronized void setSnippetsIndexed() {
        this.snippetsIndexed = true;
        System.out.println("Snippets have been indexed");
    }

    public boolean areFilesCreated(){
        return testFileCreated && trainingFileCreated;
    }
    public boolean areSnippetsIndexed(){ return snippetsIndexed; }
}
