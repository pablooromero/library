package com.library.library.services.implementations;

import com.library.library.dtos.BookDTO;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.models.Author;
import com.library.library.models.Book;
import com.library.library.repositories.AuthorRepository;
import com.library.library.repositories.BookRepository;
import com.library.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImplementation implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookDTO getBookById(Long id) throws BookNotFoundException {
        return new BookDTO(bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id)));
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public BookDTO createBook(BookDTO bookDTO) throws AuthorNotFoundException, IllegalAttributeException {
        validateBook(bookDTO);

        Book book = new Book();
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setEditorial(bookDTO.getEditorial());
        book.setPages(bookDTO.getPages());

        Long authorId = bookDTO.getAuthorId();
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + authorId));

        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return new BookDTO(savedBook);
    }


    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) throws BookNotFoundException, AuthorNotFoundException, IllegalAttributeException {
        validateBook(bookDTO);

        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setEditorial(bookDTO.getEditorial());
        existingBook.setPages(bookDTO.getPages());

        Long authorId = bookDTO.getAuthorId();
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + authorId));
        existingBook.setAuthor(author);

        Book savedBook = bookRepository.save(existingBook);
        return new BookDTO(savedBook);
    }

    @Override
    public void deleteBook(Long id) throws BookNotFoundException {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public void validateBook(BookDTO bookDTO) throws IllegalAttributeException {

        if (bookDTO.getTitle() == null || bookDTO.getTitle().trim().isEmpty()) {
            throw new IllegalAttributeException("Book title cannot be null or empty");
        }

        if (bookDTO.getIsbn() == null) {
            throw new IllegalAttributeException("ISBN cannot be null");
        }

        if (bookDTO.getPages() == null || bookDTO.getPages() <= 0) {
            throw new IllegalAttributeException("Number of pages must be greater than zero");
        }

        if (bookDTO.getAuthorId() == null) {
            throw new IllegalAttributeException("Author ID cannot be null");
        }
    }

}
