package com.nikolar.snippetbackend.mapper;

import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.model.Snippet;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class SnippetMapper {
    public Snippet dtoToEntity(SnippetDto dto){
        if (dto == null){
            return null;
        }
        Snippet entity = new Snippet();
        entity.setId(dto.getId());
        entity.setText(dto.getText());
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
}
