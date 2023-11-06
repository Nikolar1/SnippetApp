package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

@Service
public class LearningService {
    private Classifier classifier;
    private Instances data;

    public void loadClassifier() {
        System.out.println("Checking Classifier...");
        if (FileWatcher.getInstance().isTrainingComplete() && classifier == null) {
            try {
                ConverterUtils.DataSource dt = new ConverterUtils.DataSource(FileWatcher.getInstance().getTestFileName());
                data = dt.getDataSet();
                data.setClassIndex(data.numAttributes() - 1);
                data.deleteAttributeAt(1);
                classifier = new SoftVotingClassifer();
                classifier.buildClassifier(data);
                FileWatcher.getInstance().setClassifierReady();
                /*FileWatcher.getInstance().printMessageWithTime( "Started evaluating" );
                evaluateOnTestSet(classifier);*/
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Instance createInstance(String[] values) {
        if (data == null) return null;
        Instance instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            if (value.equals("")) continue;
            Attribute att = data.attribute(i);
            if (att.isNumeric()) {
                instance.setValue(att, Double.parseDouble(value));
            } else {
                instance.setValue(att, value);
            }
        }
        return instance;
    }

    public String evaluateInstance(String[] values){
        try {
            return data.classAttribute().value((int)classifier.classifyInstance(createInstance(values)));
        }catch (Exception e){

        }
        return "";
    }
    private String evaluateOnTestSet(Classifier classifier){
        try {
            Evaluation evaluation = new Evaluation(data);
            evaluation.evaluateModel(classifier,data);
            System.out.println("Evaluation results: " + evaluation.toSummaryString());
            return evaluation.toSummaryString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
