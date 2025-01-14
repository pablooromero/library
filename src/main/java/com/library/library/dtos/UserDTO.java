package com.library.library.dtos;

import com.library.library.models.UserEntity;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;

    public UserDTO() {}
    public UserDTO(UserEntity user) {
        id = user.getId();
        name = user.getName();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
        email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
