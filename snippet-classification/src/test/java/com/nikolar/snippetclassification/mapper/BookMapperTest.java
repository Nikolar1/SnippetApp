package com.nikolar.snippetclassification.mapper;

import com.nikolar.snippetclassification.dto.AuthorDto;
import com.nikolar.snippetclassification.dto.BookDto;
import com.nikolar.snippetclassification.model.Author;
import com.nikolar.snippetclassification.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class BookMapperTest {

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDtoToEntity() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName("Test Book");
        bookDto.setForTraining(true);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("Test Author");
        bookDto.setAuthor(authorDto);
        Mockito.when(authorMapper.dtoToEntity(authorDto)).thenReturn(new Author());

        // When
        Book book = bookMapper.dtoToEntity(bookDto);

        // Then
        Assertions.assertNotNull(book);
        Assertions.assertEquals(1L, book.getId());
        Assertions.assertEquals("Test Book", book.getName());
        Assertions.assertTrue(book.isForTraining());
    }

    @Test
    public void testDtoToEntityList() {
        // Given
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setName("Test Book");
        bookDto.setForTraining(true);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName("Test Author");
        bookDto.setAuthor(authorDto);
        List<BookDto> bookDtoList = Collections.singletonList(bookDto);
        Mockito.when(authorMapper.dtoToEntity(authorDto)).thenReturn(new Author());

        // When
        List<Book> books = bookMapper.dtoToEntity(bookDtoList);

        // Then
        Assertions.assertNotNull(books);
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals(1L, books.get(0).getId());
        Assertions.assertEquals("Test Book", books.get(0).getName());
        Assertions.assertTrue(books.get(0).isForTraining());
    }

    @Test
    public void testEntityToDto() {
        // Given
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setForTraining(true);
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        book.setAuthor(author);
        Mockito.when(authorMapper.entityToDto(author)).thenReturn(new AuthorDto());

        // When
        BookDto bookDto = bookMapper.entityToDto(book);

        // Then
        Assertions.assertNotNull(bookDto);
        Assertions.assertEquals(1L, bookDto.getId());
        Assertions.assertEquals("Test Book", bookDto.getName());
        Assertions.assertTrue(bookDto.isForTraining());
    }

    @Test
    public void testEntityToDtoList() {
        // Given
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setForTraining(true);
        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        book.setAuthor(author);
        List<Book> bookList = Collections.singletonList(book);
        Mockito.when(authorMapper.entityToDto(author)).thenReturn(new AuthorDto());

        // When
        List<BookDto> bookDtoList = bookMapper.entityToDto(bookList);

        // Then
        Assertions.assertNotNull(bookDtoList);
        Assertions.assertEquals(1, bookDtoList.size());
        Assertions.assertEquals(1L, bookDtoList.get(0).getId());
        Assertions.assertEquals("Test Book", bookDtoList.get(0).getName());
        Assertions.assertTrue(bookDtoList.get(0).isForTraining());
    }
}
