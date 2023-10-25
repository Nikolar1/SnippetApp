package com.nikolar.gutenbergbooksparser;

import com.nikolar.snippetbackend.SpringContext;
import com.nikolar.snippetbackend.learning.LearningService;
import com.nikolar.snippetbackend.learning.SoftVotingLearningThread;
import com.nikolar.snippetbackend.lucene.LuceneConfig;
import com.nikolar.snippetbackend.lucene.LuceneWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileWatcher {
    private static  FileWatcher instance;
    private final long startTime = System.currentTimeMillis();
    private boolean testFileCreated;
    private boolean trainingFileCreated;
    private String testFileName;
    private String trainingFileName;
    private  boolean snippetsIndexed;
    private boolean trainingComplete;
    private boolean classifierReady;

    public String getTestFileName() {
        return testFileName;
    }

    private FileWatcher(){
        testFileCreated = false;
        trainingFileCreated = false;
        snippetsIndexed = false;
        trainingComplete = false;
        classifierReady = false;
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
            try {
                SoftVotingLearningThread softVotingLearningThread = new SoftVotingLearningThread("./" + trainingFileName, "./" + testFileName);
                softVotingLearningThread.start();
            } catch (Exception e) {
                System.out.println("Error while learning");
                e.printStackTrace();
            }

        }
    }

    //Delete index directory so the files are re-indexed each time
    public void deleteIndexDirectory(){
        try {
            Path directory = Paths.get(LuceneConfig.INDEX_PATH);
            if(directory.toFile().exists()) {
                Files.walk(directory)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(java.io.File::delete);

                System.out.println("Directory deleted successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting directory: " + e.getMessage());
        }
    }

    public void printTime(){
        System.out.println((((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public void printMessageWithTime( String message ){
        System.out.println( message + " in " + ( ( ( System.currentTimeMillis() - startTime ) * 1.0 ) / 1000 ) + " seconds" );
    }

    public synchronized void setSnippetsIndexed() {
        this.snippetsIndexed = true;
        System.out.println("Snippets have been indexed in " + (((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
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

    public boolean areFilesCreated(){
        return testFileCreated && trainingFileCreated;
    }
    public boolean areSnippetsIndexed(){ return snippetsIndexed; }

    public boolean isTrainingComplete(){
        return trainingComplete;
    }
    public boolean isClassifierReady(){ return classifierReady; }
}
