package com.nikolar.snippetsearch.lucene;


public class IndexerThread extends Thread{
    private LuceneWriter lc;
    public IndexerThread(LuceneWriter lc){
        this.lc = lc;
    }
    public void run(){
        String[] snippet;
        while ((snippet = lc.nextInstance()) != null) {
            lc.indexSnippet(snippet[0], snippet[1], snippet[2].replace("-"," "));
        }
    }
}
