package com.nikolar.snippetbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isForTraining;
    @ManyToOne
    private Author author;
    @OneToMany(mappedBy = "book")
    private List<Snippet> snippets;
}
