package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import org.springframework.boot.info.JavaInfo;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.CharacterNGramTokenizer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

enum LearningFilesState{
    NO_FILE_EXIST,
    WORD_NGRAM_FILE_EXISTS,
    CHARACTER_NGRAM_FILE_EXISTS,
    MERGED_FILE_EXISTS,
    ATTRIBUTE_SELECTION_FILE_EXISTS,
    RESAMPLED_FILE_EXISTS,
    MODEL_EXISTS
}

public class LearningThread extends Thread{
    //Set to 100 for whole dataset
    private final double PERCENTAGE_OF_TRAINING_SET_TO_TRAIN_ON = 25;
    //Set to 0 for unlimited
    private final int NUMBER_OF_ATTRIBUTES_TO_KEEP = 2000;
    private Instances training;
    private Instances test;
    private Instances data;
    private int trainingDataLength;
    private Classifier classifier;

    public static void saveInstancesToARFF(Instances data, String filePath) throws Exception {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(filePath));
        saver.writeBatch();
    }
    public LearningThread(String trainingFileName, String testFileName) throws Exception {
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

    private boolean mergeInstances(File wordNgramFile, File characterNGramFile) throws IOException {
        ArffLoader loaderNgram = new ArffLoader();
        ArffLoader loaderCharNgram = new ArffLoader();
        loaderNgram.setFile(wordNgramFile);
        loaderCharNgram.setFile(characterNGramFile);
        Instances filteredData1 = loaderNgram.getStructure();
        Instances filteredData2 = loaderCharNgram.getStructure();
        ArrayList<Integer> skipIndex = new ArrayList<>();
        Instance instance1 = loaderNgram.getNextInstance(filteredData1);
        Instance instance2 = loaderCharNgram.getNextInstance(filteredData2);
        ArrayList<Attribute> atributes = new ArrayList<Attribute>();
        for (int j = 0; j < instance1.numAttributes(); j++) {
            atributes.add(instance1.attribute(j));
        }

        for (int j = 1; j < instance2.numAttributes(); j++) {
            if(!atributes.contains(instance2.attribute(j))) {
                atributes.add(instance2.attribute(j));
            }else {
                skipIndex.add(j);
            }
        }
        data = new Instances("MergedData", atributes, data.numInstances());
        while (instance1 != null) {
            if(instance2 == null){
                System.out.println("Unequal number of instances in merging sets");
                return false;
            }
            Instance mergedInstance = new DenseInstance(instance1.numAttributes() + instance2.numAttributes() - 1 - skipIndex.size());

            for (int j = 0; j < instance1.numAttributes(); j++) {
                mergedInstance.setValue(j, instance1.value(j));
            }

            int k = 0;
            for (int j = 0; j < instance2.numAttributes()-2; j++) {
                if (!skipIndex.contains(j)){
                    mergedInstance.setValue(instance1.numAttributes() + j - k, instance2.value(j-k + 1));
                }else {
                    k++;
                }
            }
            data.add(mergedInstance);
            instance1 = loaderNgram.getNextInstance(filteredData1);
            instance2 = loaderCharNgram.getNextInstance(filteredData2);
        }
        try {
            saveInstancesToARFF(data,"data/merged.arff");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void wordTok(){
        StringToWordVector f = new StringToWordVector();
        NGramTokenizer ng = new NGramTokenizer();
        ng.setNGramMaxSize(5);
        ng.setNGramMinSize(2);
        f.setTokenizer(ng);
        try {
            f.setInputFormat(data);
            saveInstancesToARFF(Filter.useFilter(data, f),"data/ngram.arff");
        }catch (Exception e){
            System.out.println("Error while applying word n-gram");
            e.printStackTrace();
        }
    }

    private void characterTok(){
        StringToWordVector f = new StringToWordVector();
        CharacterNGramTokenizer ng = new CharacterNGramTokenizer();
        ng.setNGramMaxSize(3);
        ng.setNGramMinSize(1);
        f.setTokenizer(ng);
        try {
            f.setInputFormat(data);
            saveInstancesToARFF(Filter.useFilter(data, f),"data/characterngram.arff");
        }catch (Exception e){
            System.out.println("Error while applying character n-gram");
            e.printStackTrace();
        }
    }

    private void selectAttributes() throws Exception {
        // Create the attribute selection filter
        AttributeSelection filter = new AttributeSelection();

        InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
        filter.setEvaluator(evaluator);

        Ranker ranker = new Ranker();
        if( NUMBER_OF_ATTRIBUTES_TO_KEEP > 0 ) {
            ranker.setNumToSelect( NUMBER_OF_ATTRIBUTES_TO_KEEP );
        }
        filter.setSearch(ranker);

        // Apply the filter
        filter.setInputFormat(data);
        saveInstancesToARFF(Filter.useFilter(data, filter) ,"data/attributeselectedinfogain.arff");
    }

    //Due to hardware limitations I have to cut some instances
    private void cutAndBalanceTrainingSetInstances() {

        Resample resample = new Resample();
        try {
            resample.setInputFormat(training);
            resample.setBiasToUniformClass( 1 );
            resample.setSampleSizePercent( PERCENTAGE_OF_TRAINING_SET_TO_TRAIN_ON );
            training = Filter.useFilter(training, resample);
            saveInstancesToARFF(training,"data/resampled.arff");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean buildSMO() {
        File nfile = new File("data/ngram.arff");
        File cnfile = new File("data/characterngram.arff");
        switch (checkLearningFilesState()){
            default:
                FileWatcher.getInstance().printMessageWithTime( "Started creating character ngram" );
                characterTok();
            case CHARACTER_NGRAM_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Started creating word ngram" );
                wordTok();
            case WORD_NGRAM_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Character and word ngrams created" );
                try {
                    if( !mergeInstances(nfile, cnfile) ){
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case MERGED_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Merged file created" );
                try {
                    ConverterUtils.DataSource dt = new ConverterUtils.DataSource("data/merged.arff");
                    data = dt.getDataSet();
                    data.setClassIndex(0);
                    selectAttributes();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            case ATTRIBUTE_SELECTION_FILE_EXISTS:
                FileWatcher.getInstance().printMessageWithTime( "Finished Attribute selection" );
        }


        try {
            ConverterUtils.DataSource dt = new ConverterUtils.DataSource("data/attributeselectedinfogain.arff");
            data = dt.getDataSet();
            data.setClassIndex(data.numAttributes()-1);
            int trainSetSize = trainingDataLength;
            int testSetSize = data.numInstances() - trainSetSize;
            training = new Instances(data, 0, trainSetSize);
            test = new Instances(data, trainSetSize, testSetSize);
            training.setClassIndex(training.numAttributes()-1);
            test.setClassIndex(training.numAttributes()-1);
            data = null;
            cutAndBalanceTrainingSetInstances();
            classifier = new SMO();
            FileWatcher.getInstance().printMessageWithTime( "Started building Clasifier" );
            classifier.buildClassifier(training);
            FileWatcher.getInstance().setTrainingComplete();
            String pathToSaveModel = "./data/smoinfoGain.model";
            SerializationHelper.write(pathToSaveModel, classifier);
            Evaluation evaluation = new Evaluation(test);

            // Evaluate the classifier on the test set
            evaluation.evaluateModel(classifier, test);

            // Print the evaluation results
            System.out.println("Evaluation results: " + evaluation.toSummaryString());
            FileWatcher.getInstance().printTime();
            return true;
        }catch (Exception e){
            this.classifier = null;
            System.out.println("Error while building classifier");
            e.printStackTrace();
        }

        return false;
    }

    private LearningFilesState checkLearningFilesState(){
        File nfile = new File("data/ngram.arff");
        File cnfile = new File("data/characterngram.arff");
        File mfile = new File("data/merged.arff");
        File asfile = new File("data/attributeselectedinfogain.arff");
        File modelFile = new File("./data/smoinfoGain.model");
        if (modelFile.exists()){
            return LearningFilesState.MODEL_EXISTS;
        }
        if(asfile.exists()) {
            return LearningFilesState.ATTRIBUTE_SELECTION_FILE_EXISTS;
        }
        if(mfile.exists()) {
            return LearningFilesState.MERGED_FILE_EXISTS;
        }
        if(nfile.exists()) {
            return LearningFilesState.WORD_NGRAM_FILE_EXISTS;
        }
        if(cnfile.exists()) {
            return LearningFilesState.CHARACTER_NGRAM_FILE_EXISTS;
        }
        return LearningFilesState.NO_FILE_EXIST;
    }

    public String classifiySnippet( String author ){
        String res = "";


        return res;
    }

    public void run(){
        FileWatcher.getInstance().printMessageWithTime( "Started Learning thread" );
        String pathToSavedModel = "./data/smoinfoGain.model";
        File file = new File(pathToSavedModel);
        if (file.exists()) {
            try {
                this.classifier = (Classifier) SerializationHelper.read(pathToSavedModel);
                FileWatcher.getInstance().setTrainingComplete();
            } catch (Exception e) {
                System.out.println("Error while loading classifier");
                e.printStackTrace();
            }
        }else {
            if ( buildSMO() ){
                FileWatcher.getInstance().setTrainingComplete();
            }else {
                System.out.println("Error while building classifier");
            }
        }
        FileWatcher.getInstance().printMessageWithTime( "Finnished learning" );
    }
}
