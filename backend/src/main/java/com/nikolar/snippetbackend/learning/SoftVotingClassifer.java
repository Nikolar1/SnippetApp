package com.nikolar.snippetbackend.learning;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.core.*;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.util.Arrays;

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
        Instance w = tokenizeInstanceToWNG(instance);
        Instance c = tokenizeInstanceToCNG(instance);
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

    public Instance tokenizeInstanceToWNG( Instance instance ){
        String value = instance.stringValue(0);
        DenseInstance denseInstance = new DenseInstance(wordNGDataset.numAttributes());
        for(int i = 0; i < wordNGDataset.numAttributes(); i++){
            int count = 0;
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = value.indexOf(wordNGDataset.attribute(i).name(), lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += wordNGDataset.attribute(i).name().length();
                }
            }
            denseInstance.setValue( i, count);
        }
        denseInstance.setDataset(wordNGDataset);
        return denseInstance;
    }

    public Instance tokenizeInstanceToCNG( Instance instance ){
        String value = instance.stringValue(0);
        DenseInstance denseInstance = new DenseInstance(characterNGDataset.numAttributes());
        for(int i = 0; i < characterNGDataset.numAttributes(); i++){
            int count = 0;
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = value.indexOf(characterNGDataset.attribute(i).name(), lastIndex);
                if (lastIndex != -1) {
                    count++;
                    lastIndex += characterNGDataset.attribute(i).name().length();
                }
            }
            denseInstance.setValue( i, count);
        }
        denseInstance.setDataset(characterNGDataset);
        return denseInstance;
    }
}
