package com.nikolar.snippetparser.gutenbergbooksparser;

import com.nikolar.snippetparser.dto.AuthorDto;
import com.nikolar.snippetparser.dto.BookDto;
import com.nikolar.snippetparser.dto.SnippetDto;
import com.nikolar.snippetparser.service.AuthorService;
import com.nikolar.snippetparser.service.BookService;
import com.nikolar.snippetparser.service.SnippetService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

@Component
@NoArgsConstructor
public class DataConverter {
    private String outputFileName;
    private BufferedWriter output;
    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;
    @Autowired
    SnippetService snippetService;

    private class authorWriteThread extends Thread{
        private DataParser dp;
        private AuthorDto authorDto;
        private String[] books;
        private boolean isForTraining;
        public authorWriteThread(String[] books, String trainingPath, String author, boolean isForTraining){
            dp = new DataParser(trainingPath,author);
            authorDto = new AuthorDto();
            authorDto.setName(author);
            authorDto = authorService.saveAuthor(authorDto);
            this.books = books;
            this.isForTraining = isForTraining;
        }
        public void run(){
            try {
                writeAuthor(books.length, books);
            }catch (IOException e){
                System.out.println("Error while writing author: " + dp.getAuthor());
                e.printStackTrace();
            }
        }
        private void writeAuthor(int length, String[] books) throws IOException {
            for (int j = 0; j<length; j++) {
                dp.SetFile(books[j]);
                BookDto bookDto = new BookDto();
                bookDto.setAuthor(authorDto);
                bookDto.setName(books[j].replace(".txt",""));
                bookDto.setForTraining(isForTraining);
                bookDto = bookService.saveBook(bookDto);
                LinkedList<String> parsedText = dp.Parse();
                for (int i = 0; i < parsedText.size(); i++) {
                    SnippetDto snippetDto = new SnippetDto();
                    snippetDto.setBook(bookDto);
                    snippetDto.setText(parsedText.get(i));
                    snippetService.saveSnippet(snippetDto);
                    //writeLineAndGoToNext(parsedText.get(i));
                }
            }
        }

    }

    public void setOutputFileName(String outputFileName){
        this.outputFileName = outputFileName;
    }

    private synchronized void writeLineAndGoToNext(String line)throws IOException {
        if(output == null) return;
        output.append(line);
        output.newLine();
    }
    private synchronized void writeLine(String line) throws IOException {
        if(output == null) return;
        output.append(line);
    }

    private synchronized void newLine() throws IOException {
        if(output == null) return;
        output.newLine();
    }

    private void writeAuthor(DataParser dp, int length, String[] books) throws IOException {
        for (int j = 0; j<length; j++) {
            dp.SetFile(books[j]);
            LinkedList<String> parsedText = dp.Parse();
            for (int i = 0; i < parsedText.size(); i++) {
                writeLine(parsedText.get(i));
                newLine();
            }
        }
    }

    private void writeArffHeader(String name, String[] attributes, String[] classes) throws IOException {
        writeLine("@RELATION " + name);
        newLine();
        for (int i = 0; i<attributes.length; i++) {
            newLine();
            writeLine("@attribute " + attributes[i]);
            newLine();
        }
        String classLine = "@attribute class {";
        for (int i = 0; i<classes.length; i++) {
            classLine += classes[i];
            if (i != classes.length-1){
                classLine+= ",";
            }
        }
        writeLine(classLine + "}");
        newLine();
        newLine();
        writeLine("@data");
        newLine();
    }
    public void saveToDatabaseTraining(){
        try {
            authorWriteThread awt1 = new authorWriteThread(SourceLists.EbersGeorgTraining, SourceLists.TrainingPaths[0], SourceLists.Authors[0], true);
            authorWriteThread awt2 = new authorWriteThread(SourceLists.TolstoyLeoTraining, SourceLists.TrainingPaths[1], SourceLists.Authors[1], true);
            authorWriteThread awt3 = new authorWriteThread(SourceLists.GeorgeOSmithTraining, SourceLists.TrainingPaths[2], SourceLists.Authors[2], true);
            authorWriteThread awt4 = new authorWriteThread(SourceLists.StrangHerbertTraining, SourceLists.TrainingPaths[3], SourceLists.Authors[3], true);
            authorWriteThread awt5 = new authorWriteThread(SourceLists.WebsterFrankVTraining, SourceLists.TrainingPaths[4], SourceLists.Authors[4], true);
            awt1.start();
            awt2.start();
            awt3.start();
            awt4.start();
            awt5.start();
            awt1.join();
            awt2.join();
            awt3.join();
            awt4.join();
            awt5.join();
        }catch (InterruptedException e){
            System.out.println("Error while writing training snippets to database");
            e.printStackTrace();
        }
    }

    public void saveToDatabaseTest(){
        try {
            authorWriteThread awt1 = new authorWriteThread(SourceLists.EbersGeorgTest, SourceLists.TrainingPaths[0] + "Test/", SourceLists.Authors[0],false);
            authorWriteThread awt2 = new authorWriteThread(SourceLists.TolstoyLeoTest, SourceLists.TrainingPaths[1] + "Test/", SourceLists.Authors[1],false);
            authorWriteThread awt3 = new authorWriteThread(SourceLists.GeorgeOSmithTest, SourceLists.TrainingPaths[2] + "Test/", SourceLists.Authors[2],false);
            authorWriteThread awt4 = new authorWriteThread(SourceLists.StrangHerbertTest, SourceLists.TrainingPaths[3] + "Test/", SourceLists.Authors[3],false);
            authorWriteThread awt5 = new authorWriteThread(SourceLists.WebsterFrankVTest, SourceLists.TrainingPaths[4] + "Test/", SourceLists.Authors[4],false);
            awt1.start();
            awt2.start();
            awt3.start();
            awt4.start();
            awt5.start();
            awt1.join();
            awt2.join();
            awt3.join();
            awt4.join();
            awt5.join();
        }catch (InterruptedException e){
            System.out.println("Error while writing test snippets to database");
            e.printStackTrace();
        }
    }
    /*public String toArffTraining(){
        //Passes through pre-defined list of books for training by author and adds them to {outputFileName}-training.arff
        try {
            output = new BufferedWriter(new FileWriter(outputFileName + "-training.arff", false));
            String[] attributes = {"Text string","Book string"};
            writeArffHeader("writer-training", attributes,SourceLists.Authors);
            authorWriteThread awt1 = new authorWriteThread(SourceLists.EbersGeorgTraining, SourceLists.TrainingPaths[0], SourceLists.Authors[0]);
            authorWriteThread awt2 = new authorWriteThread(SourceLists.TolstoyLeoTraining, SourceLists.TrainingPaths[1], SourceLists.Authors[1]);
            authorWriteThread awt3 = new authorWriteThread(SourceLists.GeorgeOSmithTraining, SourceLists.TrainingPaths[2], SourceLists.Authors[2]);
            authorWriteThread awt4 = new authorWriteThread(SourceLists.StrangHerbertTraining, SourceLists.TrainingPaths[3], SourceLists.Authors[3]);
            authorWriteThread awt5 = new authorWriteThread(SourceLists.WebsterFrankVTraining, SourceLists.TrainingPaths[4], SourceLists.Authors[4]);
            awt1.start();
            awt2.start();
            awt3.start();
            awt4.start();
            awt5.start();
            awt1.join();
            awt2.join();
            awt3.join();
            awt4.join();
            awt5.join();
            output.close();
        }catch (IOException | InterruptedException e){
            System.out.println("Error while writing arff training file");
            e.printStackTrace();
            return null;
        }
        String rez = outputFileName + "-training.arff";
        return rez;
    }

    public String toArffTest(){
        //Passes through pre-defined list of books for testing by author and adds them to {outputFileName}-test.arff
        try {
            output = new BufferedWriter(new FileWriter(outputFileName + "-test.arff", false));
            String[] attributes = {"Text string","Book string"};
            writeArffHeader("writer-test", attributes,SourceLists.Authors);
            authorWriteThread awt1 = new authorWriteThread(SourceLists.EbersGeorgTest, SourceLists.TrainingPaths[0] + "Test/", SourceLists.Authors[0]);
            authorWriteThread awt2 = new authorWriteThread(SourceLists.TolstoyLeoTest, SourceLists.TrainingPaths[1] + "Test/", SourceLists.Authors[1]);
            authorWriteThread awt3 = new authorWriteThread(SourceLists.GeorgeOSmithTest, SourceLists.TrainingPaths[2] + "Test/", SourceLists.Authors[2]);
            authorWriteThread awt4 = new authorWriteThread(SourceLists.StrangHerbertTest, SourceLists.TrainingPaths[3] + "Test/", SourceLists.Authors[3]);
            authorWriteThread awt5 = new authorWriteThread(SourceLists.WebsterFrankVTest, SourceLists.TrainingPaths[4] + "Test/", SourceLists.Authors[4]);
            awt1.start();
            awt2.start();
            awt3.start();
            awt4.start();
            awt5.start();
            awt1.join();
            awt2.join();
            awt3.join();
            awt4.join();
            awt5.join();
            output.close();
        }catch (IOException | InterruptedException e){
            System.out.println("Error while writing arff test file");
            e.printStackTrace();
            return null;
        }

        String rez = outputFileName + "-test.arff";
        return rez;
    }*/
}
