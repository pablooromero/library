package com.library.library.services;

import com.library.library.dtos.AuthorDTO;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    AuthorDTO getAuthorById(Long id) throws AuthorNotFoundException;

    Author saveAuthor(Author author);

    AuthorDTO createAuthor(AuthorDTO authorDTO) throws IllegalAttributeException;
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) throws AuthorNotFoundException, IllegalAttributeException;

    void deleteAuthor(Long id) throws AuthorNotFoundException;
    void validateAuthor(AuthorDTO authorDTO) throws IllegalAttributeException;
}
