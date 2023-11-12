package com.nikolar.snippetparser.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SnippetDto {
    private Long id;
    private String text;
    private BookDto book;
    private int textHashCode;

    public void generateTextHashCode(String text, String book, String author){
        textHashCode = 43 * (41 * (37 + author.hashCode()) + book.hashCode()) + text.hashCode();
    }
}
