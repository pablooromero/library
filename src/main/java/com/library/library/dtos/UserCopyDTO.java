package com.library.library.dtos;

import com.library.library.models.UserCopy;

import java.time.LocalDate;

public class UserCopyDTO {
    private Long id;
    private Long userId;
    private Long copyId;
    private LocalDate borrowedAt;
    private LocalDate returnedAt;

    public UserCopyDTO() {}

    public UserCopyDTO(UserCopy userCopy) {
        id = userCopy.getId();
        userId = userCopy.getUser() != null ? userCopy.getUser().getId() : null;
        copyId = userCopy.getCopy() != null ? userCopy.getCopy().getId() : null;
        borrowedAt = userCopy.getBorrowedDate();
        returnedAt = userCopy.getReturnedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCopyId() {
        return copyId;
    }

    public void setCopyId(Long copyId) {
        this.copyId = copyId;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }
}
