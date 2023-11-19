package com.nikolar.snippetparser.repository;

import com.nikolar.snippetparser.model.ServiceStatus;
import org.springframework.data.repository.CrudRepository;

public interface ServiceStatusRepository extends CrudRepository<ServiceStatus, Long> {
    ServiceStatus findById(long id);
    ServiceStatus findTopByOrderByIdDesc();
}
