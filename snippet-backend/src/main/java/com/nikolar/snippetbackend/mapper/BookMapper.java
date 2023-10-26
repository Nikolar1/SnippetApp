package com.nikolar.snippetbackend.mapper;

import com.nikolar.snippetbackend.dto.BookDto;
import com.nikolar.snippetbackend.model.Book;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class BookMapper {

    @Autowired
    private SnippetMapper snippetMapper;
    public Book dtoToEntity(BookDto dto){
        if (dto == null){
            return null;
        }
        Book entity = new Book();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setForTraining(dto.isForTraining());
        entity.setSnippets(snippetMapper.dtoToEntity(dto.getSnippets()));
        return entity;
    }

    public List<Book> dtoToEntity(List<BookDto> dto){
        if (dto == null){
            return null;
        }
        if (dto.isEmpty()){
            return new LinkedList<Book>();
        }
        List<Book> entities = new LinkedList<Book>();
        for (BookDto bookDto : dto){
            entities.add(dtoToEntity(bookDto));
        }
        return entities;
    }
}
