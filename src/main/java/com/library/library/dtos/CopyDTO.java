package com.library.library.dtos;

import com.library.library.models.Copy;

public class CopyDTO {
    private Long id;
    private String location;
    private Long bookId;

    public CopyDTO() {}
    public CopyDTO(Copy copy) {
        id = copy.getId();
        location = copy.getLocation();
        bookId = copy.getBook() != null ? copy.getBook().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
