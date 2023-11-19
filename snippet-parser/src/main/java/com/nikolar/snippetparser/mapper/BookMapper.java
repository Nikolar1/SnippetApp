package com.nikolar.snippetparser.mapper;

import com.nikolar.snippetparser.dto.BookDto;
import com.nikolar.snippetparser.model.Book;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
public class BookMapper {

    @Autowired
    private AuthorMapper authorMapper;
    public Book dtoToEntity(BookDto dto){
        if (dto == null){
            return null;
        }
        Book entity = new Book();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setForTraining(dto.isForTraining());
        entity.setAuthor(authorMapper.dtoToEntity(dto.getAuthor()));
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

    public BookDto entityToDto(Book entity){
        if (entity == null){
            return null;
        }
        BookDto dto = new BookDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setForTraining(entity.isForTraining());
        dto.setAuthor(authorMapper.entityToDto(entity.getAuthor()));
        return dto;
    }

    public List<BookDto> entityToDto(List<Book> entities){
        if (entities == null){
            return null;
        }
        if (entities.isEmpty()){
            return new LinkedList<BookDto>();
        }
        List<BookDto> dtos = new LinkedList<>();
        for (Book entity : entities){
            dtos.add(entityToDto(entity));
        }
        return dtos;
    }

}
