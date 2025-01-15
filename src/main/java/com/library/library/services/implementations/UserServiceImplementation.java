package com.library.library.services.implementations;

import com.library.library.config.SecurityUtils;
import com.library.library.dtos.AuthResponseDTO;
import com.library.library.dtos.ChangePasswordDTO;
import com.library.library.dtos.UserDTO;
import com.library.library.enums.RoleEnum;
import com.library.library.exceptions.AccessDeniedException;
import com.library.library.exceptions.IllegalAttributeException;
import com.library.library.exceptions.UserNotFoundException;
import com.library.library.models.UserEntity;
import com.library.library.repositories.UserRepository;
import com.library.library.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("User not found with ID: " + id) ));
    }

    @Override
    public UserDTO getUserProfile(Long id, Authentication authentication) throws UserNotFoundException {
        UserEntity user = securityUtils.getAuthenticatedUser(authentication);

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        return new UserDTO(userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("User not found with ID: " + id) ));
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws IllegalAttributeException {
        validateUser(userDTO);

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setAddress(userDTO.getAddress());

        UserEntity savedUser = userRepository.save(userEntity);
        return new UserDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) throws UserNotFoundException, IllegalAttributeException {
        validateUser(userDTO);

        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException("User not found with ID: " + id));


        existingUser.setName(userDTO.getName());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setAddress(userDTO.getAddress());

        UserEntity savedUser = userRepository.save(existingUser);
        return new UserDTO(savedUser);
    }

    @Override
    public UserDTO updateProfile(Long id, UserDTO userDTO, Authentication authentication) throws IllegalAttributeException, UserNotFoundException {
        validateUser(userDTO);

        UserEntity user = securityUtils.getAuthenticatedUser(authentication);

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());

        UserEntity savedUser = saveUser(user);
        return new UserDTO(savedUser);
    }

    @Override
    public UserDTO createAdmin(UserDTO userDTO) throws IllegalAttributeException {
        validateUser(userDTO);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setName(userDTO.getName());
        userEntity.setPhoneNumber(userDTO.getPhoneNumber());
        userEntity.setRole(RoleEnum.ADMIN);

        UserEntity savedUser = saveUser(userEntity);
        return new UserDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        if(!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByUser(Long id, Authentication authentication) throws UserNotFoundException {
        UserEntity user = securityUtils.getAuthenticatedUser(authentication);

        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        userRepository.deleteById(id);
    }

    @Override
    public AuthResponseDTO changePassword(ChangePasswordDTO changePasswordDTO, Authentication authentication) throws UserNotFoundException {
        UserEntity user = securityUtils.getAuthenticatedUser(authentication);

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            return new AuthResponseDTO("-", "Current password is incorrect");
        }

        if (changePasswordDTO.getNewPassword().length() < 8) {
            return new AuthResponseDTO("-", "New password must be at least 8 characters long");
        }

        if (passwordEncoder.matches(changePasswordDTO.getNewPassword(), user.getPassword())) {
            return new AuthResponseDTO("-", "New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        saveUser(user);

        return new AuthResponseDTO("-", "Password updated successfully");
    }

    @Override
    public void validateUser(UserDTO userDTO) throws IllegalAttributeException {
        if (userDTO.getName() == null || userDTO.getName().trim().isEmpty()) {
            throw new IllegalAttributeException("User name cannot be null or empty");
        }

        if (userDTO.getPhoneNumber() == null || userDTO.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalAttributeException("Phone number cannot be null or empty");
        }

        String phonePattern = "^\\+?\\d{1,3}?[-.\\s]?(\\(?\\d{1,4}\\)?[-.\\s]?)*\\d{1,9}$\n";
        if (!Pattern.matches(phonePattern, userDTO.getPhoneNumber())) {
            throw new IllegalAttributeException("Invalid phone number format");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
