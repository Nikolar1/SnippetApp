package com.nikolar.snippetparser.repository;

import com.nikolar.snippetparser.model.Book;
import com.nikolar.snippetparser.model.Snippet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    Snippet findById(long id);
    List<Snippet> findByText(String text);
    List<Snippet> findByTextHashCode(Integer textHashCode);
    List<Snippet> findByBook(Book book);
}
