package com.nikolar.snippetparser.service;

import com.nikolar.snippetparser.dto.SnippetDto;
import com.nikolar.snippetparser.mapper.SnippetMapper;
import com.nikolar.snippetparser.model.Snippet;
import com.nikolar.snippetparser.repository.BookRepository;
import com.nikolar.snippetparser.repository.SnippetRepository;
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

    public List<SnippetDto> getSnippetByText(String text){
        return snippetMapper.entityToDto(snippetRepository.findByText(text));
    }

    public List<SnippetDto> getSnippetsByBookId(long id){
        return snippetMapper.entityToDto(snippetRepository.findByBook(bookRepository.findById(id)));
    }

    public SnippetDto saveSnippet(SnippetDto snippetDto){
        List<Snippet> snippets = snippetRepository.findByText(snippetDto.getText());
        if (snippets != null && !snippets.isEmpty()){
            return snippetMapper.entityToDto(snippets.get(0));
        }
        Snippet snippet = snippetMapper.dtoToEntity(snippetDto);
        return snippetMapper.entityToDto(snippetRepository.save(snippet));
    }

    public SnippetDto checkSnippetIntegrity(SnippetDto snippetDto){
        List<Snippet> snippets = snippetRepository.findByTextHashCode(snippetDto.getTextHashCode());
        if (snippets != null && !snippets.isEmpty()){
            return snippetMapper.entityToDto(snippets.get(0));
        }
        Snippet snippet = snippetMapper.dtoToEntity(snippetDto);
        return snippetMapper.entityToDto(snippetRepository.save(snippet));
    }
}
