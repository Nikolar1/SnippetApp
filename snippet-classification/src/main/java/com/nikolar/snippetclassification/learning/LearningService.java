package com.nikolar.snippetclassification.learning;

import com.nikolar.snippetclassification.FileWatcher;
import org.springframework.scheduling.annotation.Async;
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
                //No need to taunt 11% accuracy...
                //writeEvaluation();
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
        if (classifier == null)
            return null;
        try {
            return data.classAttribute().value((int)classifier.classifyInstance(createInstance(values)));
        }catch (Exception e){

        }
        return "";
    }
    @Async
    protected void writeEvaluation(){
        System.out.println("Evaluation results: " + evaluateOnTestSet());
    }
    private String evaluateOnTestSet(){
        if (classifier == null || data == null)
            return null;
        FileWatcher.getInstance().printMessageWithTime( "Started evaluating" );
        try {
            Evaluation evaluation = new Evaluation(data);
            evaluation.evaluateModel(classifier,data);
            FileWatcher.getInstance().printMessageWithTime( "Finished evaluating" );
            return evaluation.toSummaryString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
