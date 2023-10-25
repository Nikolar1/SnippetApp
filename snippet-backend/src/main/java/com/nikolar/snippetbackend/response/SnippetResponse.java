package com.nikolar.snippetbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SnippetResponse {
    private String author;
    private String book;
    private String snippet;
}
