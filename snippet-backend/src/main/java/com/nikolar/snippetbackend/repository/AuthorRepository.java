package com.nikolar.snippetbackend.repository;

import com.nikolar.snippetbackend.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findById(long id);

    Author findByName(String name);

}
