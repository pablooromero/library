package com.library.library.services;

import com.library.library.dtos.UserDTO;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserEntity> getAllUsers();
    UserDTO getUserById(Long id) throws UserNotFoundException;

    UserDTO getUserProfile(Long id, Authentication authentication) throws UserNotFoundException;

    UserEntity saveUser(UserEntity userEntity);

    UserDTO createUser(UserDTO userDTO) throws IllegalAttributeException;
    UserDTO updateUser(Long id, UserDTO userDTO) throws UserNotFoundException, IllegalAttributeException;

    UserDTO updateProfile(Long id, UserDTO userDTO, Authentication authentication) throws IllegalAttributeException, UserNotFoundException;

    UserDTO createAdmin(UserDTO userDTO) throws IllegalAttributeException;

    void deleteUserByUser(Long id, Authentication authentication) throws UserNotFoundException;

    void validateUser(UserDTO userDTO) throws IllegalAttributeException;
    void deleteUser(Long id) throws UserNotFoundException;

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
