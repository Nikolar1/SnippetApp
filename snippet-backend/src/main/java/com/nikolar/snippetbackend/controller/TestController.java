package com.nikolar.snippetbackend.controller;

import com.nikolar.snippetbackend.dto.AuthorDto;
import com.nikolar.snippetbackend.dto.BookDto;
import com.nikolar.snippetbackend.dto.SnippetDto;
import com.nikolar.snippetbackend.service.AuthorService;
import com.nikolar.snippetbackend.service.BookService;
import com.nikolar.snippetbackend.service.SnippetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/test/")
public class TestController {
    @Autowired
    BookService bookService;
    @Autowired
    SnippetService snippetService;
    @Autowired
    AuthorService authorService;

    @GetMapping("getBookById")
    public ResponseEntity<BookDto> getBookById(long id){
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @GetMapping("getBookByName")
    public ResponseEntity<BookDto> getBookByName(String name){
        return new ResponseEntity<>(bookService.getBookByName(name), HttpStatus.OK);
    }

    @GetMapping("getBooksByAuthorId")
    public ResponseEntity<List<BookDto>> getBooksByAuthorId(long id){
        return new ResponseEntity<>(bookService.getBooksByAuthorId(id), HttpStatus.OK);
    }

    @GetMapping("getAuthorById")
    public ResponseEntity<AuthorDto> getAuthorById(long id){
        return new ResponseEntity<>(authorService.getAuthorById(id), HttpStatus.OK);
    }

    @GetMapping("getAuthorByName")
    public ResponseEntity<AuthorDto> getAuthorByName(String name){
        return new ResponseEntity<>(authorService.getAuthorByName(name), HttpStatus.OK);
    }

    @GetMapping("getSnippetById")
    public ResponseEntity<SnippetDto> getSnippetById(long id){
        return new ResponseEntity<>(snippetService.getSnippetById(id), HttpStatus.OK);
    }

    @GetMapping("getSnippetByText")
    public ResponseEntity<SnippetDto> getSnippetByText(String text){
        return new ResponseEntity<>(snippetService.getSnippetByText(text), HttpStatus.OK);
    }

    @GetMapping("getSnippetsByBookId")
    public ResponseEntity<List<SnippetDto>> getSnippetsByBookId(long id){
        List<SnippetDto> res = snippetService.getSnippetsByBookId(id);
        return new ResponseEntity<List<SnippetDto>>(res, HttpStatus.OK);
    }

    @GetMapping("makeTestDatabase")
    public void makeTestDatabase(){
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Georg");
        AuthorDto a1 = authorService.saveAuthor(authorDto);
        authorDto = new AuthorDto();
        authorDto.setName("Frank");
        AuthorDto a2 = authorService.saveAuthor(authorDto);

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(a2);
        bookDto.setForTraining(true);
        bookDto.setName("Heretics of Dune");
        BookDto b1 = bookService.saveBook(bookDto);
        bookDto = new BookDto();
        bookDto.setAuthor(a2);
        bookDto.setForTraining(true);
        bookDto.setName("Children of Dune");
        BookDto b2 = bookService.saveBook(bookDto);
        bookDto = new BookDto();
        bookDto.setAuthor(a2);
        bookDto.setForTraining(true);
        bookDto.setName("Dune Messiah");
        BookDto b3 = bookService.saveBook(bookDto);

        bookDto = new BookDto();
        bookDto.setAuthor(a1);
        bookDto.setForTraining(true);
        bookDto.setName("A Thorny Path");
        BookDto b4 = bookService.saveBook(bookDto);
        bookDto = new BookDto();
        bookDto.setAuthor(a1);
        bookDto.setForTraining(true);
        bookDto.setName("A Word, Only a Word");
        BookDto b5 = bookService.saveBook(bookDto);
        bookDto = new BookDto();
        bookDto.setAuthor(a1);
        bookDto.setForTraining(true);
        bookDto.setName("An Egyptian Princess");
        BookDto b6 = bookService.saveBook(bookDto);

        SnippetDto snippetDto = new SnippetDto();
        snippetDto.setBook(b1);
        snippetDto.setText("When dawn touched Paul's window sill with yellow light, he sensed it through closed eyelids, opened them, hearing then the renewed");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b1);
        snippetDto.setText("The hall door opened and his mother peered in, hair like shaded bronze held with black");
        snippetService.saveSnippet(snippetDto);

        snippetDto = new SnippetDto();
        snippetDto.setBook(b2);
        snippetDto.setText("You're awake");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b2);
        snippetDto.setText("Did you sleep well?");
        snippetService.saveSnippet(snippetDto);

        snippetDto = new SnippetDto();
        snippetDto.setBook(b3);
        snippetDto.setText("Over and over and over within Paul's floating awareness the lesson rolled");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b3);
        snippetDto.setText("Another might have missed the tension, but she had trained him in the Bene Gesserit Way—in the minutiae of observation.");
        snippetService.saveSnippet(snippetDto);

        snippetDto = new SnippetDto();
        snippetDto.setBook(b4);
        snippetDto.setText("That is enough!");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b4);
        snippetDto.setText("An hour later Melissa again,");
        snippetService.saveSnippet(snippetDto);

        snippetDto = new SnippetDto();
        snippetDto.setBook(b5);
        snippetDto.setText("Only the artist’s low whistling as");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b5);
        snippetDto.setText("Heron laid by his graver and Melissa");
        snippetService.saveSnippet(snippetDto);

        snippetDto = new SnippetDto();
        snippetDto.setBook(b6);
        snippetDto.setText("and a starling, which had sat moping since the");
        snippetService.saveSnippet(snippetDto);
        snippetDto = new SnippetDto();
        snippetDto.setBook(b6);
        snippetDto.setText("a swift glance round the room she went to the door");
        snippetService.saveSnippet(snippetDto);

    }

}
