package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.dto.ServiceStatusDto;
import com.nikolar.snippetbackend.mapper.ServiceStatusMapper;
import com.nikolar.snippetbackend.model.Author;
import com.nikolar.snippetbackend.model.ServiceStatus;
import com.nikolar.snippetbackend.repository.ServiceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceStatusService {
    @Autowired
    ServiceStatusMapper serviceStatusMapper;
    @Autowired
    ServiceStatusRepository serviceStatusRepository;
    public ServiceStatusDto getServiceStatusById(long id){
        return serviceStatusMapper.entityToDto(serviceStatusRepository.findById(id));
    }

    public ServiceStatusDto getLatestServiceStatus(){
        return serviceStatusMapper.entityToDto(serviceStatusRepository.findTopByOrderByIdDesc());
    }

    public ServiceStatusDto saveServiceStatus(ServiceStatusDto serviceStatusDto){
        ServiceStatus serviceStatus = serviceStatusMapper.dtoToEntity(serviceStatusDto);
        return serviceStatusMapper.entityToDto(serviceStatusRepository.save(serviceStatus));
    }
}
