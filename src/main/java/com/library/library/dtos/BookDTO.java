package com.library.library.dtos;

import com.library.library.models.Book;

public class BookDTO {
    private Long id;
    private String title;
    private String editorial;
    private Long isbn;
    private Long pages;
    private Long authorId;

    public BookDTO() {}
    public BookDTO(Book book) {
        id = book.getId();
        title = book.getTitle();
        editorial = book.getEditorial();
        isbn = book.getIsbn();
        pages = book.getPages();
        authorId = book.getAuthor() != null ? book.getAuthor().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEditorial() {
        return editorial;
    }

    public Long getIsbn() {
        return isbn;
    }

    public Long getPages() {
        return pages;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
