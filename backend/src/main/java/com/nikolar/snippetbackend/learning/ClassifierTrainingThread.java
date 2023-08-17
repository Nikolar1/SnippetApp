package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ClassifierTrainingThread extends Thread{
    private final Instances training;
    private final Instances test;
    private final String fileNameToSave;
    private Classifier classifier;

    public ClassifierTrainingThread(Instances training, Instances test, String fileNameToSave) {
        this.training = training;
        this.test = test;
        this.fileNameToSave = fileNameToSave;
    }

    public void run(){
        classifier = new SMO();
        FileWatcher.getInstance().printMessageWithTime( "Started building Clasifier" );
        try {
            classifier.buildClassifier(training);
            SerializationHelper.write(fileNameToSave, classifier);
            Evaluation evaluation = new Evaluation(test);
            evaluation.evaluateModel(classifier, test);
            System.out.println("Evaluation results: " + evaluation.toSummaryString());
            FileWatcher.getInstance().printTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
