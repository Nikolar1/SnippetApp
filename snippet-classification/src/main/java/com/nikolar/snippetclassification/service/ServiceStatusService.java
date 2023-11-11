package com.nikolar.snippetclassification.service;

import com.nikolar.snippetclassification.dto.ServiceStatusDto;
import com.nikolar.snippetclassification.mapper.ServiceStatusMapper;
import com.nikolar.snippetclassification.model.ServiceStatus;
import com.nikolar.snippetclassification.repository.ServiceStatusRepository;
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
