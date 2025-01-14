package com.library.library.services;

import com.library.library.dtos.UserCopyDTO;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserCopy;

import java.util.List;

public interface UserCopyService {
    List<UserCopy> getAllUserCopies();
    UserCopyDTO getUserCopyById(Long id);

    UserCopy saveUserCopy(UserCopy userCopy);

    UserCopyDTO createUserCopy(UserCopyDTO userCopyDTO) throws UserNotFoundException, IllegalAttributeException;
    UserCopyDTO updateUserCopy(Long id);

    void deleteUserCopy(Long id);
    void validateUserCopy(UserCopyDTO userCopyDTO) throws IllegalAttributeException;
}
