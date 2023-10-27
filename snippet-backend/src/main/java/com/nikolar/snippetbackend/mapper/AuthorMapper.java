package com.nikolar.snippetbackend.mapper;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.model.Author;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class AuthorMapper {
    public Author dtoToEntity(AuthorDto dto){
        if (dto == null){
            return null;
        }
        Author entity = new Author();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }

    public List<Author> dtoToEntity(List<AuthorDto> dto){
        if (dto == null){
            return null;
        }
        if (dto.isEmpty()){
            return new LinkedList<Author>();
        }
        List<Author> entities = new LinkedList<Author>();
        for (AuthorDto authorDto : dto){
            entities.add(dtoToEntity(authorDto));
        }
        return entities;
    }

    public AuthorDto entityToDto(Author entity){
        if(entity == null){
            return null;
        }
        AuthorDto dto = new AuthorDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
