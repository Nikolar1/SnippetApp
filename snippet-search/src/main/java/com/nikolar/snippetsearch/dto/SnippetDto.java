package com.nikolar.snippetsearch.dto;

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
}
