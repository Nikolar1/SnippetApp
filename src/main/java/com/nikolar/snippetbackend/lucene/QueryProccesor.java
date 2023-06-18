package com.nikolar.snippetbackend.lucene;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class QueryProccesor {
    public List query(String text){
        List contentArray = new ArrayList();
        return contentArray;
    }
}
