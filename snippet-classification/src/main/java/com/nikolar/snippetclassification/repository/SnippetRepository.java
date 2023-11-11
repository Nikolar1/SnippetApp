package com.nikolar.snippetclassification.repository;

import com.nikolar.snippetclassification.model.Book;
import com.nikolar.snippetclassification.model.Snippet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    Snippet findById(long id);
    List<Snippet> findByText(String text);
    List<Snippet> findByBook(Book book);
}
