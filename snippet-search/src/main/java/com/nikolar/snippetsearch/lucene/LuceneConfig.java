package com.nikolar.snippetsearch.lucene;

import java.util.ArrayList;
import java.util.List;

public class LuceneConfig {
    public static final String INDEX_PATH = "./data/index";
    public static final int NUM_HITS = 10;
    private static LuceneConfig instance;
    private List<String> filePaths;

    public List<String> getFilePaths() {
        return filePaths;
    }

    private LuceneConfig(){
        filePaths = new ArrayList<String>();
    }

    public static synchronized LuceneConfig getInstance(){
        if (instance == null){
            instance = new LuceneConfig();
        }
        return instance;
    }

    public void addFilePath(String filePath){
        filePaths.add(filePath);
    }

}
