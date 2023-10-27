package com.nikolar.snippetbackend.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorDto {
    private long id;
    private String name;
}
