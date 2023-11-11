package com.nikolar.snippetsearch.service;

import com.nikolar.snippetsearch.exceptions.NoSnippetsFound;
import com.nikolar.snippetsearch.lucene.LuceneConfig;
import com.nikolar.snippetsearch.lucene.LuceneWriter;
import com.nikolar.snippetsearch.lucene.QueryProccesor;
import com.nikolar.snippetsearch.response.SnippetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LuceneService {
    @Autowired
    QueryProccesor queryProccesor;
    @Autowired
    LuceneWriter luceneWriter;
    public boolean indexSnippets(){
        deleteIndexDirectory();
        try {
            luceneWriter.initialize();
            luceneWriter.run();
            queryProccesor.initialize();
            return true;
        }catch (NoSnippetsFound ie){
            System.out.println("No snippets found to index");
        }catch (IOException io){
            io.printStackTrace();
        }
        return false;
    }

    public boolean snippetsIndexed(){
        return queryProccesor.isInitialized();
    }

    public List<SnippetResponse> query(String author, String book, String snippet){
        return queryProccesor.query(author, book, snippet);
    }

    public void deleteIndexDirectory(){
        try {
            Path directory = Paths.get(LuceneConfig.INDEX_PATH);
            if(directory.toFile().exists()) {
                Files.walk(directory)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(java.io.File::delete);

                System.out.println("Directory deleted successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting directory: " + e.getMessage());
        }
    }
}
