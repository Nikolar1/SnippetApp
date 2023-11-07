package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetparser.dto.SnippetDto;
import com.nikolar.snippetparser.model.Snippet;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class SnippetMapper {
    @Autowired
    private BookMapper bookMapper;
    public Snippet dtoToEntity(SnippetDto dto){
        if (dto == null){
            return null;
        }
        Snippet entity = new Snippet();
        entity.setId(dto.getId());
        entity.setText(dto.getText());
        entity.setBook(bookMapper.dtoToEntity(dto.getBook()));
        return entity;
    }

    public List<Snippet> dtoToEntity(List<SnippetDto> dto){
        if (dto == null){
            return null;
        }
        if (dto.isEmpty()){
            return new LinkedList<Snippet>();
        }
        List<Snippet> entities = new LinkedList<Snippet>();
        for (SnippetDto snippetDto : dto){
            entities.add(dtoToEntity(snippetDto));
        }
        return entities;
    }

    public SnippetDto entityToDto(Snippet entity){
        if (entity == null){
            return null;
        }
        SnippetDto dto = new SnippetDto();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setBook(bookMapper.entityToDto(entity.getBook()));
        return dto;
    }

    public List<SnippetDto> entityToDto(List<Snippet> entities){
        if (entities == null){
            return null;
        }
        if (entities.isEmpty()){
            return new LinkedList<SnippetDto>();
        }
        List<SnippetDto> dtos = new LinkedList<>();
        for (Snippet entity : entities){
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

}
