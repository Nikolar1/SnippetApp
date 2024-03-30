package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetparser.dto.BookDto;
import com.nikolar.snippetparser.dto.SnippetDto;
import com.nikolar.snippetparser.model.Book;
import com.nikolar.snippetparser.model.Snippet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class SnippetMapperTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private SnippetMapper snippetMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDtoToEntity() {
        // Given
        SnippetDto dto = new SnippetDto();
        dto.setId(1L);
        dto.setText("Test Snippet");
        BookDto bookDto = new BookDto();
        dto.setBook(bookDto);
        Mockito.when(bookMapper.dtoToEntity(bookDto)).thenReturn(new Book());

        // When
        Snippet entity = snippetMapper.dtoToEntity(dto);

        // Then
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(1L, entity.getId());
        Assertions.assertEquals("Test Snippet", entity.getText());
    }

    @Test
    public void testDtoToEntityList() {
        // Given
        SnippetDto dto = new SnippetDto();
        dto.setId(1L);
        dto.setText("Test Snippet");
        BookDto bookDto = new BookDto();
        dto.setBook(bookDto);
        List<SnippetDto> dtoList = Collections.singletonList(dto);
        Mockito.when(bookMapper.dtoToEntity(bookDto)).thenReturn(new Book());

        // When
        List<Snippet> entities = snippetMapper.dtoToEntity(dtoList);

        // Then
        Assertions.assertNotNull(entities);
        Assertions.assertEquals(1, entities.size());
        Assertions.assertEquals(1L, entities.get(0).getId());
        Assertions.assertEquals("Test Snippet", entities.get(0).getText());
    }

}
