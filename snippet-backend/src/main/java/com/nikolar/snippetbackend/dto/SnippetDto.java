package com.nikolar.snippetbackend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SnippetDto {
    private Long id;
    private String text;
}
