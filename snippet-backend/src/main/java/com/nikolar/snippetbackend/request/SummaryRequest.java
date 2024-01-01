package com.nikolar.snippetbackend.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SummaryRequest {
    List<String> text;
}
