package com.nikolar.snippetclassification.service;

import com.nikolar.snippetclassification.FileWatcher;
import com.nikolar.snippetclassification.dto.AuthorDto;
import com.nikolar.snippetclassification.dto.BookDto;
import com.nikolar.snippetclassification.dto.SnippetDto;
import com.nikolar.snippetclassification.learning.LearningService;
import com.nikolar.snippetclassification.learning.SoftVotingLearningThread;
import com.nikolar.snippetclassification.response.AuthorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class ClassificationService {

    @Autowired
    SnippetService snippetService;
    @Autowired
    BookService bookService;
    @Autowired
    AuthorService authorService;
    @Autowired
    LearningService learningService;

    private Set<AuthorDto> authorDtos;
    private Map<AuthorDto, List<BookDto>> bookDtos;
    private Map<BookDto, List<SnippetDto>> snippetDtos;
    private static final String TRAINING_FILE_NAME = "./data/trainingFile.arff";
    private static final String TEST_FILE_NAME = "./data/testFile.arff";
    public boolean train(){
        createArffFiles();
        FileWatcher.getInstance().setTestFileName(TEST_FILE_NAME);
        try {
            SoftVotingLearningThread softVotingLearningThread = new SoftVotingLearningThread(TRAINING_FILE_NAME, TEST_FILE_NAME);
            softVotingLearningThread.run();
            learningService.loadClassifier();
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public AuthorResponse predict(String snippet){
        String[] data = new String[1];
        data[0] = snippet;
        AuthorResponse authorResponse = new AuthorResponse(learningService.evaluateInstance(data));
        return authorResponse;
    }

    public boolean isReady(){
        return FileWatcher.getInstance().isClassifierReady();
    }

    private void createArffFiles(){
        authorDtos = authorService.getAllAuthors();
        bookDtos = new HashMap<>();
        snippetDtos = new HashMap<>();
        String[] classes = new String[authorDtos.size()];
        int i = 0;
        for (AuthorDto a : authorDtos){
            classes[i] = a.getName();
            List<BookDto> books = bookService.getBooksByAuthorId(a.getId());
            for (BookDto bookDto : books){
                snippetDtos.put(bookDto, snippetService.getSnippetsByBookId(bookDto.getId()));
            }
            bookDtos.put(a, books);
            i++;
        }
        try {
            BufferedWriter trainingOutput = createArffTrainingFile(classes);
            BufferedWriter testOutput = createArffTestFile(classes);
            for (AuthorDto author : authorDtos){
                for(BookDto book: bookDtos.get(author)){
                    for (SnippetDto snippet: snippetDtos.get(book)){
                        String text = snippet.getText().replace("\'", "\\\'");
                        text = text.replace("'", "\\'");
                        //Escapes all ' characters, because they represent end of an instance in .arff files
                        text = text.replace("\'", "\\\'");
                        //Standardizes all ' characters because two types are used in original books
                        //They would skew the data otherwise
                        text = text.replace("’", "\\\'");
                        if(book.isForTraining()){
                            writeLineAndGoToNext(trainingOutput,"\'" + text + "\'" +  ", " + "\'" + book.getName().replace("\'", "\\\'") + "\'" + ", " + author.getName().replace("\'", "\\\'"));
                        }else {
                            writeLineAndGoToNext(testOutput,"\'" + text + "\'" +  ", " + "\'" + book.getName().replace("\'", "\\\'") + "\'" + ", " + author.getName().replace("\'", "\\\'"));
                        }
                    }
                }
            }
            trainingOutput.close();
            testOutput.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private BufferedWriter createArffTrainingFile(String[] classes) throws IOException{
        BufferedWriter output = new BufferedWriter(new FileWriter(TRAINING_FILE_NAME, false));
        String[] attributes = {"Text string","Book string"};
        writeArffHeader(output,"writer-training", attributes, classes);
        return output;
    }

    private BufferedWriter createArffTestFile(String[] classes) throws IOException{
        BufferedWriter output = new BufferedWriter(new FileWriter(TEST_FILE_NAME, false));
        String[] attributes = {"Text string","Book string"};
        writeArffHeader(output,"writer-test", attributes, classes);
        return output;
    }

    private void writeArffHeader(BufferedWriter output, String name, String[] attributes, String[] classes) throws IOException {
        writeLine(output,"@RELATION " + name);
        newLine(output);
        for (int i = 0; i<attributes.length; i++) {
            newLine(output);
            writeLine(output,"@attribute " + attributes[i]);
            newLine(output);
        }
        String classLine = "@attribute class {";
        for (int i = 0; i<classes.length; i++) {
            classLine += classes[i];
            if (i != classes.length-1){
                classLine+= ",";
            }
        }
        writeLine(output,classLine + "}");
        newLine(output);
        newLine(output);
        writeLine(output,"@data");
        newLine(output);
    }
    private void writeLine(BufferedWriter output, String line) throws IOException {
        if(output == null) return;
        output.append(line);
    }

    private void newLine(BufferedWriter output) throws IOException {
        if(output == null) return;
        output.newLine();
    }

    private void writeLineAndGoToNext(BufferedWriter output, String line)throws IOException {
        if(output == null) return;
        output.append(line);
        output.newLine();
    }

}
