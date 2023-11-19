package com.nikolar.snippetsearch.repository;

import com.nikolar.snippetsearch.model.ServiceStatus;
import org.springframework.data.repository.CrudRepository;

public interface ServiceStatusRepository extends CrudRepository<ServiceStatus, Long> {
    ServiceStatus findById(long id);
    ServiceStatus findTopByOrderByIdDesc();
}
