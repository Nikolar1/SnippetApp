package com.nikolar.snippetbackend.lucene;

import com.nikolar.gutenbergbooksparser.FileWatcher;
import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.learning.LearningThread;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueryProccesor {

    private IndexReader indexReader;
    private IndexSearcher searcher;
    private StandardAnalyzer queryAnalyzer;
    private boolean isInitialized;

    public QueryProccesor(){
        isInitialized = false;
    }

    public void  initialize(){
        Path indexDirectoryPath = Paths.get(LuceneConfig.INDEX_PATH);
        try {
            Directory indexDir = FSDirectory.open(indexDirectoryPath);
            indexReader = DirectoryReader.open(indexDir);
            searcher = new IndexSearcher(indexReader);
            queryAnalyzer = new StandardAnalyzer();
            isInitialized = true;
        }catch (IOException e){
            System.out.println("Error while initilizing QueryProccesor");
            e.printStackTrace();
        }
    }

    private List<SnippetDto> search(List<String> field, List<String> queries) throws ParseException, IOException {
        if (field.isEmpty()){
            System.out.println("Fields where empty");
            return null;
        }
        String query = "";
        for(int i = 0; i< field.size();i++){
            query += field.get(i) + ":\"" + queries.get(i) + "\"";
            if(i+1< field.size()){
                query += " AND ";
            }
        }
        Query q = new QueryParser("snippet", queryAnalyzer).parse(query);
        TopDocs docs = searcher.search(q, LuceneConfig.NUM_HITS);
        ScoreDoc[] hits = docs.scoreDocs;
        List<SnippetDto> rez = new ArrayList<>();
        for(int i = 0; i< hits.length; i++){
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            rez.add(new SnippetDto(d.get("author"), d.get("book"), d.get("snippet")));
        }
        return rez;
    }

    public List<SnippetDto> aidedQuery(String snippet){

        return query("", "", snippet);
    }
    public List<SnippetDto> query(String author, String book, String snippet){
        if (!isInitialized){
            this.initialize();
        }
        if (!FileWatcher.getInstance().areSnippetsIndexed()){
            return null;
        }
        List<String> fields = new ArrayList<>();
        List<String> queries = new ArrayList<>();
        if(author != null && author != ""){
            fields.add("author");
            queries.add(author);
        }
        if(book != null && book != ""){
            fields.add("book");
            queries.add(book);
        }
        if(snippet != null && snippet != ""){
            fields.add("snippet");
            queries.add(snippet);
        }
        try {
            List<SnippetDto> rez = search(fields,queries);
            return rez;
        }catch (ParseException e){
            System.out.println("Error while parsing query");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Error while searching for hits");
            e.printStackTrace();
        }
        return null;
    }
}
