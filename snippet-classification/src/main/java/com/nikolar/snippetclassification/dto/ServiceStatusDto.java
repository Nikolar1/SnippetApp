package com.nikolar.snippetclassification.dto;

import com.nikolar.snippetclassification.model.Status;
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

    public boolean equalServicesStatus(ServiceStatusDto serviceStatusDto){
        if (!this.classificationService.equals(serviceStatusDto.classificationService))
            return false;
        if (!this.parserService.equals(serviceStatusDto.parserService))
            return false;
        return this.searchService.equals(serviceStatusDto.searchService);
    }
}
