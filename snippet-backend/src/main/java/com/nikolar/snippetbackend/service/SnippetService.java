package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.mapper.SnippetMapper;
import com.nikolar.snippetbackend.model.Snippet;
import com.nikolar.snippetbackend.repository.BookRepository;
import com.nikolar.snippetbackend.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnippetService {
    @Autowired
    SnippetRepository snippetRepository;
    @Autowired
    SnippetMapper snippetMapper;
    @Autowired
    BookRepository bookRepository;

    public SnippetDto getSnippetById(long id){
        return snippetMapper.entityToDto(snippetRepository.findById(id));
    }

    public SnippetDto getSnippetByText(String text){
        return snippetMapper.entityToDto(snippetRepository.findByText(text));
    }

    public List<SnippetDto> getSnippetsByBookId(long id){
        return snippetMapper.entityToDto(snippetRepository.findByBook(bookRepository.findById(id)));
    }

    public SnippetDto saveSnippet(SnippetDto snippetDto){
        Snippet snippet = snippetMapper.dtoToEntity(snippetDto);
        return snippetMapper.entityToDto(snippetRepository.save(snippet));
    }
}
