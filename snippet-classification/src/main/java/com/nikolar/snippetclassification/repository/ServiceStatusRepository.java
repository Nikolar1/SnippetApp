package com.nikolar.snippetclassification.repository;

import com.nikolar.snippetclassification.model.ServiceStatus;
import org.springframework.data.repository.CrudRepository;

public interface ServiceStatusRepository extends CrudRepository<ServiceStatus, Long> {
    ServiceStatus findById(long id);
    ServiceStatus findTopByOrderByIdDesc();
}
