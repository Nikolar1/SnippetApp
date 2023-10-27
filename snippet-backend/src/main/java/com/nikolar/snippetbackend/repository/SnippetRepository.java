package com.nikolar.snippetbackend.repository;

import com.nikolar.snippetbackend.model.Book;
import com.nikolar.snippetbackend.model.Snippet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    Snippet findById(long id);
    Snippet findByText(String text);
    List<Snippet> findByBook(Book book);
}
