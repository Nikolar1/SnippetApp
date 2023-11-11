package com.nikolar.snippetsearch.dto;

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
