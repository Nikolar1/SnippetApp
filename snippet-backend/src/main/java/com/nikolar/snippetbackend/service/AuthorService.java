package com.nikolar.snippetbackend.service;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.mapper.AuthorMapper;
import com.nikolar.snippetbackend.model.Author;
import com.nikolar.snippetbackend.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public AuthorDto saveAuthor(AuthorDto authorDto){
        Author author = authorMapper.dtoToEntity(authorDto);
        return authorMapper.entityToDto(authorRepository.save(author));
    }
}
