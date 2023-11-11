package com.nikolar.snippetsearch.repository;

import com.nikolar.snippetsearch.model.Book;
import com.nikolar.snippetsearch.model.Snippet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    Snippet findById(long id);
    List<Snippet> findByText(String text);
    List<Snippet> findByBook(Book book);
}
