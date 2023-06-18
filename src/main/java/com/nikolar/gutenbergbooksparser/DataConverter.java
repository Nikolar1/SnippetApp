package com.nikolar.gutenbergbooksparser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class DataConverter {
    private String outputFileName;

    public DataConverter(String outputFileName){
        this.outputFileName = outputFileName;
    }

    private void writeAuthor(DataParser dp, BufferedWriter output, int length, String[] books) throws IOException {
        for (int j = 0; j<length; j++) {
            dp.SetFile(books[j]);
            LinkedList<String> parsedText = dp.Parse();
            for (int i = 0; i < parsedText.size(); i++) {
                output.append(parsedText.get(i));
                output.newLine();
            }
        }
    }

    private void writeArffHeader(BufferedWriter output, String name, String[] attributes, String[] classes) throws IOException {
        output.append("@RELATION " + name);
        output.newLine();
        for (int i = 0; i<attributes.length; i++) {
            output.newLine();
            output.append("@attribute " + attributes[i]);
            output.newLine();
        }
        String classLine = "@attribute class {";
        for (int i = 0; i<classes.length; i++) {
            classLine += classes[i];
            if (i != classes.length-1){
                classLine+= ",";
            }
        }
        output.append(classLine + "}");
        output.newLine();
        output.newLine();
        output.append("@data");
        output.newLine();
    }

    public String toArffTraining(){
        DataParser dp = new DataParser(SourceLists.TrainingPaths[0], SourceLists.Authors[0]);
        //Passes through pre-defined list of books for training by author and adds them to {outputFileName}-training.arff
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName + "-training.arff", false));
            String[] attributes = {"Text string","Book string"};
            writeArffHeader(output, "writer-training", attributes,SourceLists.Authors);
            writeAuthor(dp, output, SourceLists.EbersGeorgTraining.length, SourceLists.EbersGeorgTraining);
            dp.ChangeAuthor(SourceLists.TrainingPaths[1], SourceLists.Authors[1]);
            writeAuthor(dp, output, SourceLists.TolstoyLeoTraining.length, SourceLists.TolstoyLeoTraining);
            dp.ChangeAuthor(SourceLists.TrainingPaths[2], SourceLists.Authors[2]);
            writeAuthor(dp, output, SourceLists.GeorgeOSmithTraining.length, SourceLists.GeorgeOSmithTraining);
            dp.ChangeAuthor(SourceLists.TrainingPaths[3], SourceLists.Authors[3]);
            writeAuthor(dp, output, SourceLists.StrangHerbertTraining.length, SourceLists.StrangHerbertTraining);
            dp.ChangeAuthor(SourceLists.TrainingPaths[4], SourceLists.Authors[4]);
            writeAuthor(dp, output, SourceLists.WebsterFrankVTraining.length, SourceLists.WebsterFrankVTraining);
            output.close();
        }catch (IOException e){
            System.out.println("Error while writing arff training file");
            e.printStackTrace();
            return null;
        }
        String rez = outputFileName + "-training.arff";
        return rez;
    }

    public String toArffTest(){
        DataParser dp = new DataParser(SourceLists.TrainingPaths[0] + "Test/", SourceLists.Authors[0]);
        //Passes through pre-defined list of books for testing by author and adds them to {outputFileName}-test.arff
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFileName + "-test.arff", false));
            String[] attributes = {"Text string","Book string"};
            writeArffHeader(output, "writer-test", attributes,SourceLists.Authors);
            writeAuthor(dp, output, SourceLists.EbersGeorgTest.length, SourceLists.EbersGeorgTest);
            dp.ChangeAuthor(SourceLists.TrainingPaths[1] + "Test/", SourceLists.Authors[1]);
            writeAuthor(dp, output, SourceLists.TolstoyLeoTest.length, SourceLists.TolstoyLeoTest);
            dp.ChangeAuthor(SourceLists.TrainingPaths[2] + "Test/", SourceLists.Authors[2]);
            writeAuthor(dp, output, SourceLists.GeorgeOSmithTest.length, SourceLists.GeorgeOSmithTest);
            dp.ChangeAuthor(SourceLists.TrainingPaths[3] + "Test/", SourceLists.Authors[3]);
            writeAuthor(dp, output, SourceLists.StrangHerbertTest.length, SourceLists.StrangHerbertTest);
            dp.ChangeAuthor(SourceLists.TrainingPaths[4] + "Test/", SourceLists.Authors[4]);
            writeAuthor(dp, output, SourceLists.WebsterFrankVTest.length, SourceLists.WebsterFrankVTest);
            output.close();
        }catch (IOException e){
            System.out.println("Error while writing arff test file");
            e.printStackTrace();
            return null;
        }

        String rez = outputFileName + "-test.arff";
        return rez;
    }
}
