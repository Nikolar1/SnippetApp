package com.nikolar.snippetparser.service;

import com.nikolar.snippetparser.dto.BookDto;
import com.nikolar.snippetparser.mapper.BookMapper;
import com.nikolar.snippetparser.model.Book;
import com.nikolar.snippetparser.repository.AuthorRepository;
import com.nikolar.snippetparser.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    AuthorRepository authorRepository;
    public BookDto getBookById(long id){
        return bookMapper.entityToDto(bookRepository.findById(id));
    }

    public BookDto getBookByName(String name){
        return bookMapper.entityToDto(bookRepository.findByName(name));
    }

    public List<BookDto> getBooksByAuthorId(long id){
        return bookMapper.entityToDto(bookRepository.findByAuthor(authorRepository.findById(id)));
    }

    public BookDto saveBook(BookDto bookDto){
        Book book = bookRepository.findByName(bookDto.getName());
        if (book != null){
            return bookMapper.entityToDto(book);
        }
        book = bookMapper.dtoToEntity(bookDto);

        return bookMapper.entityToDto(bookRepository.save(book));
    }
}
