package com.nikolar.snippetclassification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorDto {
    private long id;
    private String name;
}
