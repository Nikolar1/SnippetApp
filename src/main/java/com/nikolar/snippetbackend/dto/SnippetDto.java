package com.nikolar.snippetbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SnippetDto {
    private String author;
    private String book;
    private String snippet;
}
