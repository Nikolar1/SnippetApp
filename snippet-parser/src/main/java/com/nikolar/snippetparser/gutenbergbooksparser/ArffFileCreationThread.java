package com.nikolar.snippetparser.gutenbergbooksparser;

public class ArffFileCreationThread extends Thread{
    private String outputfile;
    private  boolean makeTrainingFile;
    private  boolean makeTestFile;

    public ArffFileCreationThread(String outputfile, boolean makeTestFile, boolean makeTrainingFile){
        this.outputfile = outputfile;
        this.makeTestFile = makeTestFile;
        this.makeTrainingFile = makeTrainingFile;
    }

    public void run(){
        FileWatcher fl = FileWatcher.getInstance();
        DataConverter dc = new DataConverter();
        dc.setOutputFileName(outputfile);
        if (makeTrainingFile) {
            //String trainingFileName = dc.toArffTraining();
            //fl.setTrainingFileCreated(trainingFileName);
        }
        if (makeTestFile) {
            //String testFileName = dc.toArffTest();
            //fl.setTestFileCreated(testFileName);
        }
    }
}
