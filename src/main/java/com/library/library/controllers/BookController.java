package com.library.library.controllers;

import com.library.library.dtos.BookDTO;
import com.library.library.dtos.UserCopyDTO;
import com.library.library.exceptions.AuthorNotFoundException;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.services.BookService;
import com.library.library.services.UserCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserCopyService userCopyService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOList = bookService.getAllBooks()
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws BookNotFoundException {
        BookDTO book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/borrow")
    public ResponseEntity<UserCopyDTO> borrowBook(@RequestBody UserCopyDTO userCopyDTO) throws UserNotFoundException, IllegalAttributeException {
        UserCopyDTO newUserCopy = userCopyService.createUserCopy(userCopyDTO);
        return new ResponseEntity<>(newUserCopy, HttpStatus.OK);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<UserCopyDTO> returnBook(@PathVariable Long id) {
        UserCopyDTO updatedUserCopy = userCopyService.updateUserCopy(id);
        return new ResponseEntity<>(updatedUserCopy, HttpStatus.OK);
    }
}
