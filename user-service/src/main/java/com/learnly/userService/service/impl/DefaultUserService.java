package com.learnly.userService.service.impl;

import com.learnly.userService.dto.UserDetails;
import com.learnly.userService.dto.UserRequest;
import com.learnly.userService.dto.UserResponse;
import com.learnly.userService.dto.UserUpdateRequest;
import com.learnly.userService.exception.UserNotFoundException;
import com.learnly.userService.model.User;
import com.learnly.userService.model.UserRole;
import com.learnly.userService.repository.UserRepository;
import com.learnly.userService.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setDateOfBirth(userRequest.getDateOfBirth());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setActive(true);
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);
        return populateUserResponse(savedUser);
    }

    @Override
    public UserResponse findUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        return populateUserResponse(user);
    }

    @Override
    public UserDetails findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User Not found for this email: " + email));
        return UserDetails.
                builder().
                email(user.getEmail()).
                password(user.getPassword()).
                userId(user.getId()).
                role(user.getRole().name()).
                active(user.isActive()).
                build();
    }

    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        user.setFirstName(userUpdateRequest.getFirstName());

        if (userUpdateRequest.getLastName() != null) {
            user.setLastName(userUpdateRequest.getLastName());
        }
        if (userUpdateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        }
        if (userUpdateRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(userUpdateRequest.getDateOfBirth());
        }
        User updatedUser = userRepository.save(user);

        return populateUserResponse(updatedUser);
    }


    private UserResponse populateUserResponse(User user) {
        return UserResponse.builder().
                id(user.getId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                email(user.getEmail()).
                role(user.getRole()).
                dateOfBirth(user.getDateOfBirth()).
                createdAt(user.getCreatedAt()).
                updatedAt(user.getUpdatedAt()).
                phoneNumber(user.getPhoneNumber()).
                build();
    }

}
