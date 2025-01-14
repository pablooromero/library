package com.library.library.controllers;

import com.library.library.dtos.*;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CopyService copyService;

    @Autowired
    private UserCopyService userCopyService;

    //AUTHORS ENDPOINTS
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors()
                .stream()
                .map(AuthorDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) throws AuthorNotFoundException {
        AuthorDTO author = authorService.getAuthorById(id);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) throws IllegalAttributeException {
        AuthorDTO newAuthor = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) throws IllegalAttributeException, AuthorNotFoundException {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) throws AuthorNotFoundException {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //BOOKS ENDPOINTS
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
    List<BookDTO> bookDTOList = bookService.getAllBooks()
            .stream()
            .map(BookDTO::new)
            .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws BookNotFoundException {
        BookDTO book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) throws AuthorNotFoundException, IllegalAttributeException {
        BookDTO newBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws BookNotFoundException, AuthorNotFoundException, IllegalAttributeException {
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/books/borrow")
    public ResponseEntity<UserCopyDTO> borrowBook(@RequestBody UserCopyDTO userCopyDTO) throws UserNotFoundException, IllegalAttributeException {
        UserCopyDTO newUserCopy = userCopyService.createUserCopy(userCopyDTO);
        return new ResponseEntity<>(newUserCopy, HttpStatus.OK);
    }

    @PutMapping("/books/return/{id}")
    public ResponseEntity<UserCopyDTO> returnBook(@PathVariable Long id) {
        UserCopyDTO updatedUserCopy = userCopyService.updateUserCopy(id);
        return new ResponseEntity<>(updatedUserCopy, HttpStatus.OK);
    }


    //COPIES ENDPOINTS
    @GetMapping("/copies")
    public ResponseEntity<List<CopyDTO>> getAllCopies() {
        List<CopyDTO> copyDTOList = copyService.getAllCopies()
                .stream()
                .map(CopyDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(copyDTOList, HttpStatus.OK);
    }

    @PostMapping("/copies")
    public ResponseEntity<CopyDTO> createCopy(@RequestBody CopyDTO copyDTO) throws IllegalAttributeException, BookNotFoundException {
        CopyDTO newCopy = copyService.createCopy(copyDTO);
        return new ResponseEntity<>(newCopy, HttpStatus.CREATED);
    }

    @DeleteMapping("/copies/{id}")
    public ResponseEntity<CopyDTO> deleteCopy(@PathVariable Long id) {
        copyService.deleteCopy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //USERS ENDPOINTS
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) throws UserNotFoundException {
        UserDTO user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws IllegalAttributeException {
        UserDTO newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) throws UserNotFoundException, IllegalAttributeException {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/users/create-admin")
    public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO userDTO) throws IllegalAttributeException {
        UserDTO newUser = userService.createAdmin(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
