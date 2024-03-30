package com.nikolar.snippetbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.mapper.SnippetMapper;
import com.nikolar.snippetbackend.model.Book;
import com.nikolar.snippetbackend.model.Snippet;
import com.nikolar.snippetbackend.repository.BookRepository;
import com.nikolar.snippetbackend.repository.SnippetRepository;

public class SnippetServiceTest {

    @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private SnippetMapper snippetMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SnippetService snippetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSnippetById() {
        // Mock data
        long id = 1;
        Snippet snippet = new Snippet();
        SnippetDto expectedDto = new SnippetDto(); // mock dto
        // Mock behavior
        when(snippetRepository.findById(id)).thenReturn(snippet);
        when(snippetMapper.entityToDto(snippet)).thenReturn(expectedDto);

        // Call the method to test
        SnippetDto result = snippetService.getSnippetById(id);

        // Verify the result
        assertEquals(expectedDto, result);
    }

    @Test
    public void testGetSnippetByText() {
        // Mock data
        String text = "test";
        List<Snippet> snippets = new ArrayList<>();
        SnippetDto expectedDto = new SnippetDto(); // mock dto
        // Mock behavior
        when(snippetRepository.findByText(text)).thenReturn(snippets);
        when(snippetMapper.entityToDto(snippets)).thenReturn(List.of(expectedDto));

        // Call the method to test
        List<SnippetDto> result = snippetService.getSnippetByText(text);

        // Verify the result
        assertEquals(List.of(expectedDto), result);
    }

    @Test
    public void testGetSnippetsByBookId() {
        // Mock data
        long bookId = 1;
        Book book = new Book();
        List<Snippet> snippets = new ArrayList<>();
        SnippetDto expectedDto = new SnippetDto(); // mock dto
        // Mock behavior
        when(bookRepository.findById(bookId)).thenReturn(book);
        when(snippetRepository.findByBook(book)).thenReturn(snippets);
        when(snippetMapper.entityToDto(snippets)).thenReturn(List.of(expectedDto));

        // Call the method to test
        List<SnippetDto> result = snippetService.getSnippetsByBookId(bookId);

        // Verify the result
        assertEquals(List.of(expectedDto), result);
    }

    @Test
    public void testSaveSnippet() {
        // Mock data
        SnippetDto snippetDto = new SnippetDto(); // mock dto
        List<Snippet> snippets = new ArrayList<>();
        Snippet snippet = new Snippet();
        // Mock behavior
        when(snippetMapper.dtoToEntity(snippetDto)).thenReturn(snippet);
        when(snippetRepository.findByText(snippetDto.getText())).thenReturn(snippets);
        when(snippetMapper.entityToDto(snippet)).thenReturn(snippetDto);
        when(snippetRepository.save(snippet)).thenReturn(snippet);

        // Call the method to test
        SnippetDto result = snippetService.saveSnippet(snippetDto);

        // Verify the result
        assertEquals(snippetDto, result);
    }
}
