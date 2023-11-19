package com.nikolar.snippetclassification.repository;

import com.nikolar.snippetclassification.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findById(long id);

    Author findByName(String name);

}
