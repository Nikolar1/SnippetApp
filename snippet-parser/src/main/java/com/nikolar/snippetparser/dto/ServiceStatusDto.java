package com.nikolar.snippetparser.dto;

import com.nikolar.snippetbackend.model.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceStatusDto {
    private Long id;
    private LocalDateTime dateTime;
    private Status classificationService;
    private Status parserService;
    private Status searchService;
}
