package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetparser.dto.AuthorDto;
import com.nikolar.snippetparser.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    public void setUp() {
        authorMapper = new AuthorMapper();
    }

    @Test
    public void testDtoToEntity_WithNonNullDto() {
        // Arrange
        AuthorDto dto = new AuthorDto();
        dto.setId(1L);
        dto.setName("Test Author");

        // Act
        Author result = authorMapper.dtoToEntity(dto);

        // Assert
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    public void testDtoToEntity_WithNullDto() {
        AuthorDto req = null;
        // Act
        Author result = authorMapper.dtoToEntity(req);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDtoListToEntityList_WithNonNullDtoList() {
        // Arrange
        List<AuthorDto> dtoList = new ArrayList<>();
        AuthorDto dto1 = new AuthorDto();
        dto1.setId(1L);
        dto1.setName("Test Author 1");
        AuthorDto dto2 = new AuthorDto();
        dto2.setId(2L);
        dto2.setName("Test Author 2");
        dtoList.add(dto1);
        dtoList.add(dto2);

        // Act
        List<Author> resultList = authorMapper.dtoToEntity(dtoList);

        // Assert
        assertEquals(dtoList.size(), resultList.size());
        for (int i = 0; i < dtoList.size(); i++) {
            AuthorDto dto = dtoList.get(i);
            Author entity = resultList.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getName(), entity.getName());
        }
    }

    @Test
    public void testDtoListToEntityList_WithNullDtoList() {
        List<AuthorDto> authors = null;
        // Act
        List<Author> resultList = authorMapper.dtoToEntity(authors);

        // Assert
        assertNull(resultList);
    }

    @Test
    public void testEntityToDto_WithNonNullEntity() {
        // Arrange
        Author entity = new Author();
        entity.setId(1L);
        entity.setName("Test Author");

        // Act
        AuthorDto result = authorMapper.entityToDto(entity);

        // Assert
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    public void testEntityToDto_WithNullEntity() {
        Author author = null;
        // Act
        AuthorDto result = authorMapper.entityToDto(author);

        // Assert
        assertNull(result);
    }
}
