package com.nikolar.snippetclassification.learning;

import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class LearningConfig {
    public static final double PERCENTAGE_OF_TRAINING_SET_TO_TRAIN_ON = 100;
    public static final String WORD_N_GRAM_FILE = "data/ngram.arff";
    public static final String CHARACTER_N_GRAM_FILE = "data/characterngram.arff";
    public static final String WORD_N_GRAM_MODEL_FILE = "./data/smoWNG.model";
    public static final String CHARACTER_N_GRAM_MODEL_FILE = "./data/smoCNG.model";
    public static final double VOTING_WEIGHT_FOR_WORD_N_GRAM = 1.0;
    public static final double VOTING_WEIGHT_FOR_CHARACTER_N_GRAM = 1.0;
    public static final String FILTER_FOR_WORD_N_GRAM = "./data/filterWNG.model";
    public static final String FILTER_FOR_CHARACTER_N_GRAM = "./data/filterCNG.model";

}
