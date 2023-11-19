package com.nikolar.snippetparser.repository;

import com.nikolar.snippetparser.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findById(long id);

    Author findByName(String name);

}
