package com.nikolar.snippetbackend.response;

import com.nikolar.snippetbackend.model.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusResponse {
    private Status classificationService;
    private Status parserService;
    private Status searchService;
    private LocalDateTime localDateTime;
}
