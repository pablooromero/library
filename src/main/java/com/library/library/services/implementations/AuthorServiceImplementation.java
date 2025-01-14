package com.library.library.services.implementations;

import com.library.library.dtos.AuthorDTO;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.models.Author;
import com.library.library.repositories.AuthorRepository;
import com.library.library.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImplementation implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public AuthorDTO getAuthorById(Long id) throws AuthorNotFoundException {
        return new AuthorDTO(authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + id)));
    }

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDTO) throws IllegalAttributeException {
        validateAuthor(authorDTO);

        Author author = new Author();
        author.setName(authorDTO.getName());

        saveAuthor(author);
        return new AuthorDTO(author);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) throws AuthorNotFoundException, IllegalAttributeException {
        validateAuthor(authorDTO);

        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + id));

        existingAuthor.setName(authorDTO.getName());

        saveAuthor(existingAuthor);
        return new AuthorDTO(existingAuthor);
    }

    @Override
    public void deleteAuthor(Long id) throws AuthorNotFoundException {
        if(!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("Author not found with ID: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public void validateAuthor(AuthorDTO authorDTO) throws IllegalAttributeException {
        if (authorDTO.getName().trim().isEmpty()) {
            throw new IllegalAttributeException("Author name cannot be null or empty");
        }
    }
}
