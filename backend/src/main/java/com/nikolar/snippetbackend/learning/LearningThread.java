package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.*;
import weka.core.converters.ConverterUtils;
import weka.core.tokenizers.CharacterNGramTokenizer;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.ArrayList;

public class LearningThread extends Thread{

    private interface Callback {
        void onDataReceived(Instances data);
    }

    private class DataFilter extends Thread {
        private Callback callback;
        private Instances data;
        private int tokenizer;

        public DataFilter(Instances data, int tokenizer, Callback callback) {
            this.callback = callback;
            this.data = data;
            this.tokenizer = tokenizer;
        }

        private void wordTok(){
            StringToWordVector f = new StringToWordVector();
            NGramTokenizer ng = new NGramTokenizer();
            ng.setNGramMaxSize(5);
            ng.setNGramMinSize(1);
            f.setTokenizer(ng);
            try {
                f.setInputFormat(data);
                callback.onDataReceived(Filter.useFilter(data, f));
                return ;
            }catch (Exception e){
                System.out.println("Error while applying word n-gram");
                e.printStackTrace();
            }
            callback.onDataReceived(null);
        }

        private void characterTok(){
            StringToWordVector f = new StringToWordVector();
            CharacterNGramTokenizer ng = new CharacterNGramTokenizer();
            ng.setNGramMaxSize(3);
            ng.setNGramMinSize(1);
            f.setTokenizer(ng);
            try {
                f.setInputFormat(data);
                callback.onDataReceived(Filter.useFilter(data, f));
                return;
            }catch (Exception e){
                System.out.println("Error while applying character n-gram");
                e.printStackTrace();
            }
            callback.onDataReceived(null);
        }

        @Override
        public void run() {
            if (tokenizer == 1){
                wordTok();
            }
            if (tokenizer == -1){
                characterTok();
            }
        }
    }

    private Instances characterNGram;
    private Instances wordNgram;
    private Instances training;
    private Instances test;
    private Classifier classifier;
    public LearningThread(String trainingFileName, String testFileName) throws Exception {
        ConverterUtils.DataSource dt = new ConverterUtils.DataSource(trainingFileName);
        training = dt.getDataSet();
        training.setClassIndex(training.numAttributes() -1);
        //We don't need the book name it would be too easy to clasify
        training.deleteAttributeAt(1);
        dt = new ConverterUtils.DataSource(testFileName);
        test = dt.getDataSet();
        test.setClassIndex(test.numAttributes() -1);
        test.deleteAttributeAt(1);
    }

    private Instances mergeInstances(){
        Instances filteredData1 = new Instances(wordNgram);
        Instances filteredData2 = new Instances(characterNGram);
        Instances mergedData = new Instances("MergedData", new ArrayList<Attribute>(), training.numInstances());
        for (int i = 0; i < filteredData1.numInstances(); i++) {
            Instance instance1 = filteredData1.instance(i);
            Instance instance2 = filteredData2.instance(i);

            Instance mergedInstance = new DenseInstance(instance1.numAttributes() + instance2.numAttributes());

            // Copy attribute values from instance1 to mergedInstance
            for (int j = 0; j < instance1.numAttributes(); j++) {
                mergedInstance.setValue(j, instance1.value(j));
            }

            // Copy attribute values from instance2 to mergedInstance
            for (int j = 0; j < instance2.numAttributes(); j++) {
                mergedInstance.setValue(instance1.numAttributes() + j, instance2.value(j));
            }

            // Add mergedInstance to mergedData
            mergedData.add(mergedInstance);
        }
        return mergedData;
    }

    private boolean buildSMO() {
        DataFilter trainingDataFilter1 = new DataFilter(new Instances(training), 1, new Callback() {
            @Override
            public void onDataReceived(Instances data) {
                wordNgram = data;
            }
        });
        DataFilter trainingDataFilter2 = new DataFilter(new Instances(training), -1, new Callback() {
            @Override
            public void onDataReceived(Instances data) {
                characterNGram = data;
            }
        });
        trainingDataFilter1.start();
        trainingDataFilter2.start();
        try {
            trainingDataFilter1.join();
            trainingDataFilter2.join();
        }catch (InterruptedException e){

        }

        Instances mergedData = mergeInstances();
        wordNgram = null;
        characterNGram = null;
        training = null;
        test = null;
        System.out.println(mergedData);
        classifier = new SMO();
        try {
            System.out.println("Started building Clasifier.....");
            FileWatcher.getInstance().printTime();
            classifier.buildClassifier(mergedData);
            FileWatcher.getInstance().setTrainingComplete();
            String pathToSaveModel = "./data/smo.model";
            SerializationHelper.write(pathToSaveModel, classifier);
        }catch (Exception e){
            this.classifier = null;
            System.out.println("Error while building classifier");
            e.printStackTrace();
        }

        return false;
    }

    public void run(){
        String pathToSavedModel = "./data/smo.model";
        File file = new File(pathToSavedModel);
        if (file.exists()) {
            try {
                Classifier loadedClassifier = (Classifier) SerializationHelper.read(pathToSavedModel);
            } catch (Exception e) {
                System.out.println("Error while loading classifier");
                e.printStackTrace();
            }
        }else {
            buildSMO();
        }

    }
}
