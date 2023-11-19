package com.nikolar.snippetparser.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto {
    private long id;
    private String name;
    private boolean isForTraining;
    private AuthorDto author;
}
