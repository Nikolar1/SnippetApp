package com.nikolar.snippetbackend.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LuceneWriter {

    private IndexWriter indexer;
    public  LuceneWriter() throws IOException {
        LuceneConfig lc = LuceneConfig.getInstance();
        Path indexDirectoryPath = Paths.get(LuceneConfig.INDEX_PATH);
        Directory indexDir = FSDirectory.open(indexDirectoryPath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc = iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexer = new IndexWriter(indexDir, iwc);
    }

    public boolean indexSnippet(String snippet, String author, String book) throws IOException{
        if(indexer== null){
            return false;
        }
        Document doc = new Document();
        doc.add(new TextField("snippet", snippet, Field.Store.YES));
        doc.add(new TextField("author", author, Field.Store.YES));
        doc.add(new TextField("book", book, Field.Store.YES));
        indexer.addDocument(doc);
        return true;
    }

    public void closeIndexer() throws IOException{
        if (indexer == null){
            return;
        }
        indexer.close();
    }
}
