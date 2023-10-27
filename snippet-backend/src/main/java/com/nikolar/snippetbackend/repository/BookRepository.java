package com.nikolar.snippetbackend.repository;

import com.nikolar.snippetbackend.model.Author;
import com.nikolar.snippetbackend.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findById(long id);
    Book findByName(String name);
    List<Book> findByAuthor(Author author);
}
