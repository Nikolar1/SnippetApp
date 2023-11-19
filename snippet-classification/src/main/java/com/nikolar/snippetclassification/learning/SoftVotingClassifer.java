package com.nikolar.snippetclassification.learning;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.*;
import weka.core.converters.ConverterUtils;

public class SoftVotingClassifer implements Classifier, CapabilitiesHandler {

    private SMO wordNGClassifier;
    private SMO characterNGClassifier;
    private Instances wordNGDataset;
    private Instances characterNGDataset;



    public SoftVotingClassifer() {
    }

    @Override
    public void buildClassifier(Instances instances) throws Exception {
        this.wordNGClassifier = (SMO) SerializationHelper.read( LearningConfig.WORD_N_GRAM_MODEL_FILE );
        this.characterNGClassifier = (SMO) SerializationHelper.read( LearningConfig.CHARACTER_N_GRAM_MODEL_FILE );

        ConverterUtils.DataSource dt = new ConverterUtils.DataSource( LearningConfig.WORD_N_GRAM_FILE );
        Instances data = dt.getDataSet();
        data.setClassIndex( 0 );
        wordNGDataset = new Instances( data, 0, 1 );

        dt = new ConverterUtils.DataSource( LearningConfig.CHARACTER_N_GRAM_FILE );
        data = dt.getDataSet();
        data.setClassIndex( 0 );
        characterNGDataset = new Instances( data, 0, 1 );
    }

    @Override
    public double classifyInstance(Instance instance) throws Exception {
        double[] distribution = distributionForInstance(instance);
        int rez = 0;
        for(int i = 1; i < distribution.length; i++){
            if(distribution[rez] < distribution[i]){
                rez = i;
            }
        }
        return rez * 1.0;
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        Instance w = tokenizeInstance( instance, wordNGDataset );
        Instance c = tokenizeInstance( instance, characterNGDataset );
        double[] distForWordNG = wordNGClassifier.distributionForInstance(w);
        double[] distForCharNG = characterNGClassifier.distributionForInstance(c);
        double[] rez = new double[distForWordNG.length];
        for(int i = 0; i < distForWordNG.length; i++){
            rez[i] = distForCharNG[i] * LearningConfig.VOTING_WEIGHT_FOR_CHARACTER_N_GRAM + distForWordNG[i] * LearningConfig.VOTING_WEIGHT_FOR_WORD_N_GRAM;
        }
        return rez;
    }

    @Override
    public Capabilities getCapabilities() {
        Capabilities result =  new PolyKernel().getCapabilities();
        result.setOwner( this );
        result.enableAllAttributeDependencies();
        if (result.handles(Capabilities.Capability.NUMERIC_ATTRIBUTES)) {
            result.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES);
        }

        result.enable(Capabilities.Capability.MISSING_VALUES);
        result.disableAllClasses();
        result.disableAllClassDependencies();
        result.disable(Capabilities.Capability.NO_CLASS);
        result.enable(Capabilities.Capability.NOMINAL_CLASS);
        result.enable(Capabilities.Capability.MISSING_CLASS_VALUES);
        return result;
    }

    //Makes Bag of Words representation of instance that's same as dataset
    public Instance tokenizeInstance( Instance instance, Instances dataset ){
        String value = instance.stringValue(0);
        DenseInstance denseInstance = new DenseInstance(dataset.numAttributes());
        for(int i = 0; i < dataset.numAttributes(); i++){
            int count = 0;
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = value.indexOf(dataset.attribute(i).name(), lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += dataset.attribute(i).name().length();
                }
            }
            denseInstance.setValue( i, count);
        }
        denseInstance.setDataset(dataset);
        return denseInstance;
    }
}
