package com.library.library.controllers;

import com.library.library.dtos.BookDTO;
import com.library.library.exceptions.BookNotFoundException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserCopy;
import com.library.library.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/books")
public class BookController {

    @Autowired
    private BookService bookService;

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


    @GetMapping("/{bookId}/available")
    public boolean checkAvailability(@PathVariable Long bookId) throws BookNotFoundException {
        return bookService.canLendBook(bookId);
    }

    @PostMapping("/{bookId}/lend")
    public ResponseEntity<String> lendBook(@PathVariable Long bookId, Authentication authentication) throws UserNotFoundException, BookNotFoundException {
        bookService.lendBook(bookId, authentication);
        return ResponseEntity.ok("The book has been successfully lent.");
    }

    @PostMapping("/return/{userCopyId}")
    public ResponseEntity<String> returnBook(@PathVariable Long userCopyId, Authentication authentication) throws Exception {
        bookService.returnBook(userCopyId, authentication);
        return ResponseEntity.ok("The book has been returned.");
    }

    @PostMapping("/{bookId}/copies")
    public ResponseEntity<Void> addCopies(@PathVariable Long bookId, @RequestParam int count, @RequestParam String location) throws BookNotFoundException {
        bookService.addCopiesToBook(bookId, count, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
