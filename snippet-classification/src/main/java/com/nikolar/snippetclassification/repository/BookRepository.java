package com.nikolar.snippetclassification.repository;

import com.nikolar.snippetclassification.model.Author;
import com.nikolar.snippetclassification.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findById(long id);
    Book findByName(String name);
    List<Book> findByAuthor(Author author);
}
