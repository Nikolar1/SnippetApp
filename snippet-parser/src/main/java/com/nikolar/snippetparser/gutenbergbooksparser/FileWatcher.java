package com.nikolar.snippetparser.gutenbergbooksparser;


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
    }

    public synchronized void setTrainingFileCreated(String trainingFileName) {
        this.trainingFileCreated = true;
        this.trainingFileName = trainingFileName;
        System.out.println("Training arff file created in " + (((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public void printTime(){
        System.out.println((((System.currentTimeMillis() - startTime)*1.0)/1000) + " seconds");
    }

    public void printMessageWithTime( String message ){
        System.out.println( message + " in " + ( ( ( System.currentTimeMillis() - startTime ) * 1.0 ) / 1000 ) + " seconds" );
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
