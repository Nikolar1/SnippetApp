package com.nikolar.gutenbergbooksparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    //Number of lines to delete from the begining of the book
    private static int firstLinesToDelete = 35;
    //Number of lines to delete from the end of the book
    private static int lastLinesToDelete = 400;
    //Name of the current book
    private String filename;
    //Route to the book folder
    private String filepath;
    //Book Author
    private String author;
    //Book name
    private String book;
    public DataParser(String filepath, String author){
        this.filepath = filepath;
        this.author = author;
    }

    //Change of autor when going to the next one
    public void ChangeAuthor(String filepath, String author){
        this.filepath = filepath;
        this.author = author;
    }

    //Change of book when going to the next one
    public void SetFile(String filename){
        this.book = filename.replace(".txt", "");
        this.book = this.book.replace("\'", "\\\'");
        this.filename = filepath + filename;
    }

    private LinkedList<String> removeFirstAndLast(LinkedList<String> inputText){
        //Deletes excess lines from the beggining of the book
        for (int i = 0; i<firstLinesToDelete; i++){
            inputText.removeFirst();
        }
        //Deletes lines from the end of the book but checks if there are enough lines to avoid an error
        int size = Math.max(inputText.size() - lastLinesToDelete,0);
        if (size!=0) {
            for (int i = inputText.size(); i > size; i--) {
                inputText.remove(i - 1);
            }
        }
        return inputText;
    }

    private String processLine(String text){
        if (text == ""){
            return "";
        }
        //Skip snippets that contain word chapter
        if (text.contains("CHAPTER")){
            return "";
        }
        //If the current text does not contain any letters, skip it
        String regex = ".*[a-zA-Z].*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcherText = pattern.matcher(text);
        if (!matcherText.matches()){
            return "";
        }

        //Escapes all ' characters, because they represent end of an instance in .arff files
        text = text.replace("\'", "\\\'");
        //Standardizes all ' characters because two types are used in original books
        //They would skew the data otherwise
        text = text.replace("’", "\\\'");
        //Add author name to the end
        text = "\'" + text + "\'" +  ", " + "\'" + book + "\'" + ", " + author;
        return text;
    }

    public LinkedList<String> Parse(){
        if (filename == null){
            return null;
        }
        LinkedList<String> inputText = new LinkedList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            //Reads all lines from a file
            while((line = in.readLine()) != null)
            {
                inputText.add(line);
            }
            in.close();
            inputText = removeFirstAndLast(inputText);
            String text = "";
            LinkedList<String> parsedText = new LinkedList<>();
            //Passes through lines to connect and proccess them
            for (int i = 0; i<inputText.size()-1; i++){
                line = inputText.get(i);
                //Forms a snippet if it reaches the end
                if (line.trim().isEmpty()){
                    text = processLine(text);
                    if (text != "") {
                        parsedText.add(text);
                        text = "";
                    }
                }
                if (text == ""){
                    text = line;
                }else {
                    //Adds the line to current text and continues the search
                    text = text + " " + line;
                }
            }
            return parsedText;
        }catch (IOException e) {
            System.out.println("Error while reading file:");
            e.printStackTrace();
            return null;
        }
    }
}
