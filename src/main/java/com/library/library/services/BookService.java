package com.library.library.services;

import com.library.library.dtos.BookDTO;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.Book;
import com.library.library.models.UserCopy;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    BookDTO getBookById(Long id) throws BookNotFoundException;

    Book saveBook(Book book);

    BookDTO createBook(BookDTO bookDTO) throws AuthorNotFoundException, IllegalAttributeException;
    BookDTO updateBook(Long id, BookDTO bookDTO) throws BookNotFoundException, AuthorNotFoundException, IllegalAttributeException;

    void deleteBook(Long id) throws BookNotFoundException;

    boolean canLendBook(Long bookId) throws BookNotFoundException;

    UserCopy lendBook (Long bookId, Authentication authentication) throws BookNotFoundException, UserNotFoundException;

    UserCopy returnBook(Long userCopyId, Authentication authentication) throws Exception;

    void addCopiesToBook(Long bookId, int numberOfCopies, String location) throws BookNotFoundException;

    void validateBook(BookDTO bookDTO) throws IllegalAttributeException;
}
