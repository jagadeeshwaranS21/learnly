package com.learnly.userService.service;

import com.learnly.userService.dto.UserDetails;
import com.learnly.userService.dto.UserRequest;
import com.learnly.userService.dto.UserResponse;
import com.learnly.userService.dto.UserUpdateRequest;

public interface UserService {

    UserResponse registerUser(UserRequest userRequest);

    UserResponse findUserById(String userId);

    UserDetails findUserByEmail(String email);

    UserResponse updateUser(UserUpdateRequest userUpdateRequest,String userId);
}
