package com.nikolar.snippetsearch.repository;

import com.nikolar.snippetsearch.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findById(long id);

    Author findByName(String name);

}
