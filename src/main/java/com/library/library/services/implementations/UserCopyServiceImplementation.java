package com.library.library.services.implementations;

import com.library.library.dtos.UserCopyDTO;
import com.library.library.exceptions.CopyNotFoundException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserCopyNotFoundException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.Copy;
import com.library.library.models.UserCopy;
import com.library.library.models.UserEntity;
import com.library.library.repositories.CopyRepository;
import com.library.library.repositories.UserCopyRepository;
import com.library.library.repositories.UserRepository;
import com.library.library.services.UserCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserCopyServiceImplementation implements UserCopyService {

    @Autowired
    private UserCopyRepository userCopyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CopyRepository copyRepository;

    @Override
    public List<UserCopy> getAllUserCopies() {
        return userCopyRepository.findAll();
    }

    @Override
    public UserCopyDTO getUserCopyById(Long id) {
        return new UserCopyDTO(userCopyRepository.findById(id)
                .orElseThrow(() -> new UserCopyNotFoundException("UserCopy not found with ID: " + id)));
    }

    @Override
    public UserCopy saveUserCopy(UserCopy userCopy) {
        return userCopyRepository.save(userCopy);
    }

    @Override
    public UserCopyDTO createUserCopy(UserCopyDTO userCopyDTO) throws UserNotFoundException, IllegalAttributeException {
        validateUserCopy(userCopyDTO);

        UserCopy userCopy = new UserCopy();

        UserEntity user = userRepository.findById(userCopyDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userCopyDTO.getUserId()));

        Copy copy = copyRepository.findById(userCopyDTO.getCopyId())
                .orElseThrow(() -> new CopyNotFoundException("Copy not found with ID: " + userCopyDTO.getCopyId()));

        userCopy.setUser(user);
        userCopy.setCopy(copy);
        userCopy.setBorrowedDate(LocalDate.now());

        UserCopy savedUserCopy = userCopyRepository.save(userCopy);
        return new UserCopyDTO(savedUserCopy);
    }

    @Override
    public UserCopyDTO updateUserCopy(Long id) {
        UserCopy userCopy = userCopyRepository.findById(id)
                .orElseThrow(() -> new UserCopyNotFoundException("UserCopy not found with ID: " + id));

        userCopy.setReturnedDate(LocalDate.now());

        UserCopy updatedUserCopy = userCopyRepository.save(userCopy);
        return new UserCopyDTO(updatedUserCopy);
    }

    @Override
    public void deleteUserCopy(Long id) {
        if (!userCopyRepository.existsById(id)) {
            throw new UserCopyNotFoundException("UserCopy not found with ID: " + id);
        }
        userCopyRepository.deleteById(id);
    }

    @Override
    public void validateUserCopy(UserCopyDTO userCopyDTO) throws IllegalAttributeException {
        boolean isCopyLoaned = userCopyRepository.existsByCopyIdAndReturnedDateIsNull(userCopyDTO.getCopyId());
        if (isCopyLoaned) {
            throw new IllegalAttributeException("The copy is already loaned.");
        }
    }
}
