package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.BookDto;
import com.nikolar.snippetbackend.mapper.BookMapper;
import com.nikolar.snippetbackend.model.Author;
import com.nikolar.snippetbackend.model.Book;
import com.nikolar.snippetbackend.repository.AuthorRepository;
import com.nikolar.snippetbackend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBookById() {
        // Mock data
        long id = 1;
        Book book = new Book();
        book.setId(id);
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);

        // Mock behavior
        when(bookRepository.findById(id)).thenReturn(book);
        when(bookMapper.entityToDto(book)).thenReturn(expectedDto);

        // Test
        BookDto actualDto = bookService.getBookById(id);

        // Verify
        assertEquals(expectedDto, actualDto);
        verify(bookRepository, times(1)).findById(id);
        verify(bookMapper, times(1)).entityToDto(book);
    }

    @Test
    public void testGetBookByName() {
        // Mock data
        String name = "Test Book";
        Book book = new Book();
        book.setName(name);
        BookDto expectedDto = new BookDto();
        expectedDto.setName(name);

        // Mock behavior
        when(bookRepository.findByName(name)).thenReturn(book);
        when(bookMapper.entityToDto(book)).thenReturn(expectedDto);

        // Test
        BookDto actualDto = bookService.getBookByName(name);

        // Verify
        assertEquals(expectedDto, actualDto);
        verify(bookRepository, times(1)).findByName(name);
        verify(bookMapper, times(1)).entityToDto(book);
    }

    @Test
    public void testGetBooksByAuthorId() {
        // Mock data
        long authorId = 1;
        Author author = new Author();
        author.setId(authorId);
        Book book1 = new Book();
        book1.setAuthor(author);
        Book book2 = new Book();
        book2.setAuthor(author);
        List<Book> books = Arrays.asList(book1, book2);
        BookDto expectedDto1 = new BookDto();
        BookDto expectedDto2 = new BookDto();
        List<BookDto> expectedDtoList = Arrays.asList(expectedDto1, expectedDto2);

        // Mock behavior
        when(authorRepository.findById(authorId)).thenReturn(author);
        when(bookRepository.findByAuthor(author)).thenReturn(books);
        when(bookMapper.entityToDto(books)).thenReturn(expectedDtoList);

        // Test
        List<BookDto> actualDtoList = bookService.getBooksByAuthorId(authorId);

        // Verify
        assertEquals(expectedDtoList, actualDtoList);
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).findByAuthor(author);
        verify(bookMapper, times(1)).entityToDto(books);
    }

    @Test
    public void testSaveBook() {
        // Mock data
        BookDto bookDto = new BookDto();
        bookDto.setName("Test Book");
        Book book = new Book();
        book.setName("Test Book");
        BookDto expectedDto = new BookDto();
        expectedDto.setName("Test Book");

        // Mock behavior
        when(bookRepository.findByName(bookDto.getName())).thenReturn(null);
        when(bookMapper.dtoToEntity(bookDto)).thenReturn(book);
        when(bookMapper.entityToDto(book)).thenReturn(expectedDto);
        when(bookRepository.save(book)).thenReturn(book);

        // Test
        BookDto actualDto = bookService.saveBook(bookDto);

        // Verify
        assertEquals(expectedDto, actualDto);
        verify(bookRepository, times(1)).findByName(bookDto.getName());
        verify(bookMapper, times(1)).dtoToEntity(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).entityToDto(book);
    }
}
