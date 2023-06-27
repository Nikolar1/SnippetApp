package com.nikolar.snippetbackend.lucene;
import com.nikolar.gutenbergbooksparser.FileWatcher;
import weka.core.Instance;

public class IndexerThread extends Thread{
    private LuceneWriter lc;
    public IndexerThread(LuceneWriter lc){
        this.lc = lc;
    }
    public void run(){
        Instance inst;
        while ((inst = lc.nextInstance()) != null) {
            lc.indexSnippet(inst.stringValue(0), inst.stringValue(1), inst.stringValue(2));
        }
    }
}
