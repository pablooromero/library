package com.library.library.services.implementations;

import com.library.library.config.SecurityUtils;
import com.library.library.dtos.BookDTO;
import com.library.library.exceptions.*;
import com.library.library.models.*;
import com.library.library.repositories.AuthorRepository;
import com.library.library.repositories.BookRepository;
import com.library.library.repositories.CopyRepository;
import com.library.library.repositories.UserCopyRepository;
import com.library.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImplementation implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Autowired
    private UserCopyRepository userCopyRepository;

    @Autowired
    private SecurityUtils securityUtils;

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
    public boolean canLendBook(Long bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        return book.getAvailableCopiesCount() > 0;
    }

    @Override
    public UserCopy lendBook(Long bookId, Authentication authentication) throws BookNotFoundException, UserNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        Copy availableCopy = book.getCopies().stream()
                .filter(copy -> copy.getUserCopies().stream().noneMatch(userCopy -> userCopy.getReturnedDate() == null))
                .findFirst()
                .orElseThrow( () -> new BookNotFoundException("No copy available"));

        UserEntity authenticatedUser = securityUtils.getAuthenticatedUser(authentication);

        availableCopy.setCurrentUser(authenticatedUser);

        UserCopy userCopy = new UserCopy(authenticatedUser, availableCopy, LocalDate.now(), null);

        copyRepository.save(availableCopy);

        return userCopyRepository.save(userCopy);
    }

    @Override
    public UserCopy returnBook(Long userCopyId, Authentication authentication) throws Exception {
        UserEntity user = securityUtils.getAuthenticatedUser(authentication);

        UserCopy userCopy = userCopyRepository.findById(userCopyId)
                .orElseThrow(() -> new UserCopyNotFoundException("User copy not found with ID: " + userCopyId));

        if (!userCopy.getUser().getId().equals(user.getId())) {
            throw new Exception("This user did not borrow this book");
        }

        userCopy.setReturnedDate(LocalDate.now());

        Copy copy = userCopy.getCopy();
        copy.setCurrentUser(null);
        copyRepository.save(copy);

        return userCopyRepository.save(userCopy);
    }

    @Override
    public void addCopiesToBook(Long bookId, int numberOfCopies, String location) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId)
                .orElseThrow( () -> new BookNotFoundException("Book with not found with ID: " + bookId));

        for (int i = 0; i < numberOfCopies; i++) {
            Copy copy = new Copy(location);
            copy.setBook(book);
            book.getCopies().add(copy);
        }

        bookRepository.save(book);
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
