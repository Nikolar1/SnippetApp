package com.nikolar.snippetbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.mapper.AuthorMapper;
import com.nikolar.snippetbackend.model.Author;
import com.nikolar.snippetbackend.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAuthorById() {
        // Mock data
        long id = 1L;
        Author author = new Author();
        author.setId(id);
        author.setName("Test Author");

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        authorDto.setName("Test Author");

        // Mock repository method
        when(authorRepository.findById(id)).thenReturn(author);
        // Mock mapper method
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);

        // Test the service method
        AuthorDto result = authorService.getAuthorById(id);

        // Verify that repository method was called with correct parameter
        verify(authorRepository).findById(id);
        // Verify that mapper method was called with correct parameter
        verify(authorMapper).entityToDto(author);

        // Verify the result
        assertEquals(authorDto, result);
    }

    @Test
    public void testGetAuthorByName() {
        // Mock data
        String name = "Test Author";
        Author author = new Author();
        author.setId(1L);
        author.setName(name);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setName(name);

        // Mock repository method
        when(authorRepository.findByName(name)).thenReturn(author);
        // Mock mapper method
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);

        // Test the service method
        AuthorDto result = authorService.getAuthorByName(name);

        // Verify that repository method was called with correct parameter
        verify(authorRepository).findByName(name);
        // Verify that mapper method was called with correct parameter
        verify(authorMapper).entityToDto(author);

        // Verify the result
        assertEquals(authorDto, result);
    }

    @Test
    public void testSaveAuthor() {
        // Mock data
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Test Author");

        Author author = new Author();
        author.setName("Test Author");

        // Mock repository method
        when(authorRepository.findByName(authorDto.getName())).thenReturn(null);
        when(authorMapper.dtoToEntity(authorDto)).thenReturn(author);
        when(authorMapper.entityToDto(author)).thenReturn(authorDto);
        when(authorRepository.save(author)).thenReturn(author);

        // Test the service method
        AuthorDto result = authorService.saveAuthor(authorDto);

        // Verify that repository method was called with correct parameter
        verify(authorRepository).findByName(authorDto.getName());
        // Verify that mapper methods were called with correct parameters
        verify(authorMapper).dtoToEntity(authorDto);
        verify(authorMapper).entityToDto(author);
        // Verify that repository method save was called
        verify(authorRepository).save(author);

        // Verify the result
        assertEquals(authorDto, result);
    }
}
