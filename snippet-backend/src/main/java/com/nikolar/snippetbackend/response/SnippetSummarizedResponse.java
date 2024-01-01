package com.nikolar.snippetbackend.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SnippetSummarizedResponse {
    private String author;
    private String book;
    private String snippet;
    private String snippetSummary;

    public SnippetSummarizedResponse(SnippetResponse snippetResponse, String snippetSummary){
        author = snippetResponse.getAuthor();
        book = snippetResponse.getBook();
        snippet = snippetResponse.getSnippet();
        this.snippetSummary = snippetSummary;
    }
}
