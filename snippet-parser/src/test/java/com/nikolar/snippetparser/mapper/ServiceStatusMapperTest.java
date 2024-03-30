package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetparser.dto.ServiceStatusDto;
import com.nikolar.snippetparser.model.ServiceStatus;
import com.nikolar.snippetparser.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ServiceStatusMapperTest {

    private ServiceStatusMapper serviceStatusMapper;

    @BeforeEach
    public void setup() {
        serviceStatusMapper = new ServiceStatusMapper();
    }

    @Test
    public void testDtoToEntity() {
        // Given
        ServiceStatusDto dto = new ServiceStatusDto();
        dto.setId(1L);
        dto.setDateTime(LocalDateTime.now());
        dto.setSearchService(Status.RUNNING);
        dto.setClassificationService(Status.STARTING);
        dto.setParserService(Status.DOWNED);

        // When
        ServiceStatus entity = serviceStatusMapper.dtoToEntity(dto);

        // Then
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(1L, entity.getId());
        Assertions.assertEquals(dto.getDateTime(), entity.getDateTime());
        Assertions.assertEquals(Status.RUNNING, entity.getSearchService());
        Assertions.assertEquals(Status.STARTING, entity.getClassificationService());
        Assertions.assertEquals(Status.DOWNED, entity.getParserService());
    }

    @Test
    public void testEntityToDto() {
        // Given
        ServiceStatus entity = new ServiceStatus();
        entity.setId(1L);
        entity.setDateTime(LocalDateTime.now());
        entity.setSearchService(Status.RUNNING);
        entity.setClassificationService(Status.STARTING);
        entity.setParserService(Status.DOWNED);

        // When
        ServiceStatusDto dto = serviceStatusMapper.entityToDto(entity);

        // Then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals(entity.getDateTime(), dto.getDateTime());
        Assertions.assertEquals(Status.RUNNING, dto.getSearchService());
        Assertions.assertEquals(Status.STARTING, dto.getClassificationService());
        Assertions.assertEquals(Status.DOWNED, dto.getParserService());
    }
}
