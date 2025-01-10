package com.library.library.dtos;

import com.library.library.models.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorDTO {
    private Long id;
    private String name;
    private Long code;

    private List<BookDTO> books;
    public AuthorDTO() {}
    public AuthorDTO(Author author) {
        id = author.getId();
        name = author.getName();
        code = author.getCode();
        books = author.getBooks()
                .stream()
                .map(BookDTO::new)
                .toList();
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
