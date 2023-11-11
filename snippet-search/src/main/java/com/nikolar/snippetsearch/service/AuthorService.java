package com.nikolar.snippetsearch.service;

import com.nikolar.snippetsearch.dto.AuthorDto;
import com.nikolar.snippetsearch.mapper.AuthorMapper;
import com.nikolar.snippetsearch.model.Author;
import com.nikolar.snippetsearch.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    AuthorMapper authorMapper;
    public AuthorDto getAuthorById(long id){
        return authorMapper.entityToDto(authorRepository.findById(id));
    }

    public AuthorDto getAuthorByName(String name){
        return authorMapper.entityToDto(authorRepository.findByName(name));
    }

    public Set<AuthorDto> getAllAuthors(){
        Set<Author> rez = new HashSet<>();
        authorRepository.findAll().forEach(rez::add);
        return authorMapper.entityToDto(rez);
    }

    public AuthorDto saveAuthor(AuthorDto authorDto){
        Author author = authorRepository.findByName(authorDto.getName());
        if(author != null){
            return authorMapper.entityToDto(author);
        }
        author = authorMapper.dtoToEntity(authorDto);
        return authorMapper.entityToDto(authorRepository.save(author));
    }
}
