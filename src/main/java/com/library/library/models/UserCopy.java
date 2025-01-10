package com.library.library.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class UserCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate borrowedDate;
    private LocalDate returnedDate;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private Copy copy;

    public UserCopy() {}
    public UserCopy(UserEntity user, Copy copy, LocalDate borrowedDate, LocalDate returnedDate) {
        this.user = user;
        this.copy = copy;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }
}