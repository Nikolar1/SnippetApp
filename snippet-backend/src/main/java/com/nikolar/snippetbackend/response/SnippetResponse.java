package com.nikolar.snippetbackend.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SnippetResponse {
    private String author;
    private String book;
    private String snippet;
}
