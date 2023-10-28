package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetbackend.dto.ServiceStatusDto;
import com.nikolar.snippetbackend.model.ServiceStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ServiceStatusMapper {
    public ServiceStatus dtoToEntity(ServiceStatusDto dto){
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setId(dto.getId());
        serviceStatus.setDateTime(dto.getDateTime());
        serviceStatus.setSearchService(dto.getSearchService());
        serviceStatus.setClassificationService(dto.getClassificationService());
        serviceStatus.setParserService(dto.getParserService());
        return serviceStatus;
    }

    public ServiceStatusDto entityToDto(ServiceStatus serviceStatus){
        ServiceStatusDto dto = new ServiceStatusDto();
        dto.setId(serviceStatus.getId());
        dto.setDateTime(serviceStatus.getDateTime());
        dto.setSearchService(serviceStatus.getSearchService());
        dto.setClassificationService(serviceStatus.getClassificationService());
        dto.setParserService(serviceStatus.getParserService());
        return dto;
    }
}
