package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.CharacterNGramTokenizer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;

public class SoftVotingLearningThread extends Thread{
    private Instances training;
    private Instances test;
    private Instances data;
    private int trainingDataLength;
    private StringToWordVector wordNGramFilter;
    private StringToWordVector characterNGramFilter;

    public static void saveInstancesToARFF(Instances data, String filePath) throws Exception {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(filePath));
        saver.writeBatch();
    }
    public SoftVotingLearningThread(String trainingFileName, String testFileName) throws Exception {
        FileWatcher.getInstance().printMessageWithTime( "Started learning" );
        ConverterUtils.DataSource dt = new ConverterUtils.DataSource(trainingFileName);
        data = dt.getDataSet();
        data.setClassIndex(data.numAttributes() -1);
        //We don't need the book name it would be too easy to clasify
        data.deleteAttributeAt(1);
        Instances test;
        dt = new ConverterUtils.DataSource(testFileName);
        test = dt.getDataSet();
        test.setClassIndex(test.numAttributes() -1);
        test.deleteAttributeAt(1);
        trainingDataLength = data.numInstances();
        data.addAll(test);
    }

    private void wordTok(){
        wordNGramFilter = new StringToWordVector();
        NGramTokenizer ng = new NGramTokenizer();
        ng.setNGramMaxSize(5);
        ng.setNGramMinSize(2);
        wordNGramFilter.setTokenizer(ng);
        try {
            wordNGramFilter.setInputFormat(data);
            SerializationHelper.write(LearningConfig.FILTER_FOR_WORD_N_GRAM, wordNGramFilter);
            saveInstancesToARFF(Filter.useFilter(data, wordNGramFilter),"data/ngram.arff");
        }catch (Exception e){
            System.out.println("Error while applying word n-gram");
            e.printStackTrace();
        }
    }

    private void characterTok(){
        characterNGramFilter = new StringToWordVector();
        CharacterNGramTokenizer cg = new CharacterNGramTokenizer();
        cg.setNGramMaxSize(3);
        cg.setNGramMinSize(1);
        characterNGramFilter.setTokenizer(cg);
        try {
            characterNGramFilter.setInputFormat(data);
            SerializationHelper.write(LearningConfig.FILTER_FOR_CHARACTER_N_GRAM, characterNGramFilter);
            saveInstancesToARFF(Filter.useFilter(data, characterNGramFilter),"data/characterngram.arff");
        }catch (Exception e){
            System.out.println("Error while applying character n-gram");
            e.printStackTrace();
        }
    }

    //Due to hardware limitations I have to cut some instances
    private void cutAndBalanceTrainingSetInstances() {

        Resample resample = new Resample();
        try {
            resample.setInputFormat(training);
            resample.setBiasToUniformClass( 1 );
            resample.setSampleSizePercent( LearningConfig.PERCENTAGE_OF_TRAINING_SET_TO_TRAIN_ON );
            training = Filter.useFilter(training, resample);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LearningFilesState checkLearningFilesState(){
        File nfile = new File(LearningConfig.WORD_N_GRAM_FILE);
        File cnfile = new File(LearningConfig.CHARACTER_N_GRAM_FILE);
        File charModelFile = new File(LearningConfig.CHARACTER_N_GRAM_MODEL_FILE);
        File wordModelFile = new File(LearningConfig.WORD_N_GRAM_MODEL_FILE);
        if (wordModelFile.exists() && charModelFile.exists()){
            return LearningFilesState.MODEL_EXISTS;
        }
        if(nfile.exists()) {
            return LearningFilesState.WORD_NGRAM_FILE_EXISTS;
        }
        if(cnfile.exists()) {
            return LearningFilesState.CHARACTER_NGRAM_FILE_EXISTS;
        }
        return LearningFilesState.NO_FILE_EXIST;
    }

    private boolean buildClassifiers() {
        switch (checkLearningFilesState()){
            default:
                FileWatcher.getInstance().printMessageWithTime( "Started creating character ngram" );
                characterTok();
            case CHARACTER_NGRAM_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Started creating word ngram" );
                wordTok();
            case WORD_NGRAM_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Character and word ngrams created" );
        }


        try {
            ConverterUtils.DataSource dt = new ConverterUtils.DataSource( LearningConfig.WORD_N_GRAM_FILE );
            data = dt.getDataSet();
            data.setClassIndex( 0 );
            int trainSetSize = trainingDataLength;
            int testSetSize = data.numInstances() - trainSetSize;
            training = new Instances( data, 0, trainSetSize );
            test = new Instances( data, trainSetSize, testSetSize );
            data = null;
            training.setClassIndex( 0 );
            test.setClassIndex( 0 );
            cutAndBalanceTrainingSetInstances();
            ClassifierTrainingThread wordThread = new ClassifierTrainingThread(new Instances( training ), new Instances( test ), LearningConfig.WORD_N_GRAM_MODEL_FILE);
            training = null;
            test = null;

            dt = new ConverterUtils.DataSource( LearningConfig.CHARACTER_N_GRAM_FILE );
            data = dt.getDataSet();
            data.setClassIndex( data.numAttributes() - 1 );
            testSetSize = data.numInstances() - trainSetSize;
            training = new Instances( data, 0, trainSetSize );
            test = new Instances( data, trainSetSize, testSetSize );
            data = null;
            training.setClassIndex( 0 );
            test.setClassIndex( 0 );
            cutAndBalanceTrainingSetInstances();
            ClassifierTrainingThread characterThread = new ClassifierTrainingThread(new Instances( training ), new Instances( test ), LearningConfig.CHARACTER_N_GRAM_MODEL_FILE);
            training = null;
            test = null;

            wordThread.start();
            characterThread.start();

            wordThread.join();
            characterThread.join();

            return true;
        }catch (Exception e){
            System.out.println("Error while building classifier");
            e.printStackTrace();
        }

        return false;
    }

    public void run(){
        FileWatcher.getInstance().printMessageWithTime( "Started Learning thread" );
        if (checkLearningFilesState() == LearningFilesState.MODEL_EXISTS) {
            try {
                FileWatcher.getInstance().setTrainingComplete();
            } catch (Exception e) {
                System.out.println("Error while loading classifier");
                e.printStackTrace();
            }
        }else {
            if ( buildClassifiers() ){
                FileWatcher.getInstance().setTrainingComplete();
            }else {
                System.out.println("Error while building classifier");
            }
        }
        FileWatcher.getInstance().printMessageWithTime( "Finnished learning" );
    }
}
