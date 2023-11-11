package com.nikolar.snippetsearch.lucene;

import com.nikolar.snippetsearch.dto.AuthorDto;
import com.nikolar.snippetsearch.dto.BookDto;
import com.nikolar.snippetsearch.dto.SnippetDto;
import com.nikolar.snippetsearch.exceptions.NoSnippetsFound;
import com.nikolar.snippetsearch.service.AuthorService;
import com.nikolar.snippetsearch.service.BookService;
import com.nikolar.snippetsearch.service.SnippetService;
import lombok.NoArgsConstructor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@Component
@NoArgsConstructor
public class LuceneWriter{
   /* private ArffLoader.ArffReader arffReader = null;
    private Instances data;
    private int fileNumber;*/

    @Autowired
    SnippetService snippetService;
    @Autowired
    BookService bookService;
    @Autowired
    AuthorService authorService;
    private IndexWriter indexer;

    private Iterator<AuthorDto> authorDtos;
    private Iterator<BookDto> bookDtos;
    private Iterator<SnippetDto> snippetDtos;
    private AuthorDto currentAuthorDto;
    private BookDto currentBookDto;

    public void initialize() throws IOException, NoSnippetsFound {
        LuceneConfig lc = LuceneConfig.getInstance();
        Path indexDirectoryPath = Paths.get(LuceneConfig.INDEX_PATH);
        Directory indexDir = FSDirectory.open(indexDirectoryPath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc = iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        /*fileNumber = 0;
        if (lc.getFilePaths().isEmpty()){
            throw new IOException("No files set");
        }*/
        indexer = new IndexWriter(indexDir, iwc);
        boolean initializedIterators = false;
        authorDtos = authorService.getAllAuthors().iterator();
        while (!initializedIterators) {
            if (!authorDtos.hasNext()) {
                throw new NoSnippetsFound();
            }
            currentAuthorDto = authorDtos.next();
            while (!initializedIterators) {
                bookDtos = bookService.getBooksByAuthorId(currentAuthorDto.getId()).iterator();
                if (!bookDtos.hasNext()) {
                    break;
                }
                currentBookDto = bookDtos.next();
                snippetDtos = snippetService.getSnippetsByBookId(currentBookDto.getId()).iterator();
                if (snippetDtos.hasNext()){
                    initializedIterators = true;
                }

            }
        }
    }

    /*private boolean nextFile(){
        if(fileNumber < LuceneConfig.getInstance().getFilePaths().size()){
            try {
                BufferedReader in = new BufferedReader(new FileReader(LuceneConfig.getInstance().getFilePaths().get(fileNumber)));
                arffReader = new ArffLoader.ArffReader(in, 100);
                data = arffReader.getStructure();
                data.setClassIndex(data.numAttributes()-1);
                fileNumber++;
                return true;
            }catch (IOException e){
                System.out.println("Error while changing file to be indexed");
                e.printStackTrace();
            }
        }
        return false;
    }*/


    public synchronized String[] nextInstance(){
        while (true) {
            if (snippetDtos.hasNext()){
                return new String[]{snippetDtos.next().getText(), currentBookDto.getName(), currentAuthorDto.getName()};
            }
            if (!bookDtos.hasNext()) {
                if (!authorDtos.hasNext()) {
                    return null;
                }
                currentAuthorDto = authorDtos.next();
                bookDtos = bookService.getBooksByAuthorId(currentAuthorDto.getId()).iterator();
                continue;
            }
            currentBookDto = bookDtos.next();
            snippetDtos = snippetService.getSnippetsByBookId(currentBookDto.getId()).iterator();
        }
       /* if(arffReader == null){
            if(!nextFile()) {
                return null;
            }
        }
        Instance rez;
        try {
            while((rez = arffReader.readInstance(data)) == null && nextFile());
            return rez;
        } catch (IOException e) {
            System.out.println("Error while reading arff file");
            e.printStackTrace();
        }*/
    }

    public void run(){
        int numberOfThreads = Math.max(1, Runtime.getRuntime().availableProcessors()-2);
        IndexerThread[] indexingThread = new IndexerThread[numberOfThreads];
        for(int i = 0; i<numberOfThreads; i++){
            indexingThread[i] = new IndexerThread(this);
        }
        for (int i = 0; i<numberOfThreads; i++){
            indexingThread[i].start();
        }
        for (int i = 0; i<numberOfThreads; i++){
            try {
                indexingThread[i].join();
            }catch (InterruptedException e){
                System.out.println("Interupted while waiting");
                e.printStackTrace();
            }
        }
        try {
            closeIndexer();
        }catch (IOException e){
            System.out.println("Error while closing indexer");
            e.printStackTrace();
        }
    }

    //not synchronized as it works only with IndexWriter which is threadsafe
    public boolean indexSnippet(String snippet,  String book, String author){
        if(indexer == null){
            return false;
        }
        Document doc = new Document();
        doc.add(new TextField("author", author, Field.Store.YES));
        doc.add(new TextField("book", book, Field.Store.YES));
        doc.add(new TextField("snippet", snippet, Field.Store.YES));
        try {
            indexer.addDocument(doc);
            return true;
        }catch (IOException e){
            System.out.println("Error while indexing a line");
            e.printStackTrace();
            return false;
        }
    }

    private void closeIndexer() throws IOException{
        if (indexer == null){
            return;
        }
        indexer.close();
        //FileWatcher.getInstance().setSnippetsIndexed();
    }
}
