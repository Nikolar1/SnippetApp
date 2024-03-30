package com.nikolar.snippetbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nikolar.snippetbackend.dto.ServiceStatusDto;
import com.nikolar.snippetbackend.mapper.ServiceStatusMapper;
import com.nikolar.snippetbackend.model.ServiceStatus;
import com.nikolar.snippetbackend.repository.ServiceStatusRepository;

public class ServiceStatusServiceTest {

    @Mock
    private ServiceStatusMapper serviceStatusMapper;

    @Mock
    private ServiceStatusRepository serviceStatusRepository;

    @InjectMocks
    private ServiceStatusService serviceStatusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetServiceStatusById() {
        // Mock data
        long id = 1;
        ServiceStatus serviceStatus = new ServiceStatus();
        ServiceStatusDto expectedDto = new ServiceStatusDto(); // mock dto
        // Mock behavior
        when(serviceStatusRepository.findById(id)).thenReturn(serviceStatus);
        when(serviceStatusMapper.entityToDto(serviceStatus)).thenReturn(expectedDto);

        // Call the method to test
        ServiceStatusDto result = serviceStatusService.getServiceStatusById(id);

        // Verify the result
        assertEquals(expectedDto, result);
    }

    @Test
    public void testGetLatestServiceStatus() {
        // Mock data
        ServiceStatus serviceStatus = new ServiceStatus();
        ServiceStatusDto expectedDto = new ServiceStatusDto(); // mock dto
        // Mock behavior
        when(serviceStatusRepository.findTopByOrderByIdDesc()).thenReturn(serviceStatus);
        when(serviceStatusMapper.entityToDto(serviceStatus)).thenReturn(expectedDto);

        // Call the method to test
        ServiceStatusDto result = serviceStatusService.getLatestServiceStatus();

        // Verify the result
        assertEquals(expectedDto, result);
    }

    @Test
    public void testSaveServiceStatus() {
        // Mock data
        ServiceStatusDto serviceStatusDto = new ServiceStatusDto(); // mock dto
        ServiceStatus serviceStatus = new ServiceStatus();
        // Mock behavior
        when(serviceStatusMapper.dtoToEntity(serviceStatusDto)).thenReturn(serviceStatus);
        when(serviceStatusRepository.save(serviceStatus)).thenReturn(serviceStatus);
        when(serviceStatusMapper.entityToDto(serviceStatus)).thenReturn(serviceStatusDto);

        // Call the method to test
        ServiceStatusDto result = serviceStatusService.saveServiceStatus(serviceStatusDto);

        // Verify the result
        assertEquals(serviceStatusDto, result);
    }
}
