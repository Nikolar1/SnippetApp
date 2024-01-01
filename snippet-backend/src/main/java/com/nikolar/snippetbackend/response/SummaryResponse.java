package com.nikolar.snippetbackend.response;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SummaryResponse {
    private String status;
    private List<String> summary_text;
}
