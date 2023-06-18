package com.nikolar.snippetbackend.lucene;
import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class IndexerThread extends Thread{
    public IndexerThread(){

    }
    public void run(){
        List<String> filePaths = LuceneConfig.getInstance().getFilePaths();
        try {
            LuceneWriter lc = new LuceneWriter();
            for(String path : filePaths){
                BufferedReader in = new BufferedReader(new FileReader(path));
                ArffLoader.ArffReader arffReader = new ArffLoader.ArffReader(in, 100);
                Instances data = arffReader.getStructure();
                data.setClassIndex(data.numAttributes() - 1);
                Instance inst;
                while ((inst = arffReader.readInstance(data))!=null){
                    //Arff file instances are in the form of snippet, book, author
                    lc.indexSnippet(inst.stringValue(0), inst.stringValue(1), inst.stringValue(2));
                }
            }
            lc.closeIndexer();
            FileWatcher.getInstance().setSnippetsIndexed();
        }catch (IOException e){
            System.out.println("Error while indexing files");
            e.printStackTrace();
        }
    }
}
