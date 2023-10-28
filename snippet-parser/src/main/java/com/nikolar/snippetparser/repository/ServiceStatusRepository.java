package com.nikolar.snippetparser.repository;

import com.nikolar.snippetbackend.model.ServiceStatus;
import org.springframework.data.repository.CrudRepository;

public interface ServiceStatusRepository extends CrudRepository<ServiceStatus, Long> {
    ServiceStatus findById(long id);
    ServiceStatus findTopByOrderByIdDesc();
}
