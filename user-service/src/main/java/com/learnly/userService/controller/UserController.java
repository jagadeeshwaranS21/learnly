package com.learnly.userService.controller;

import com.learnly.userService.dto.UserDetails;
import com.learnly.userService.dto.UserRequest;
import com.learnly.userService.dto.UserResponse;
import com.learnly.userService.dto.UserUpdateRequest;
import com.learnly.userService.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }

    @GetMapping("getUserById/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping("findUserByEmail/{email}")
    public ResponseEntity<UserDetails> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PatchMapping("updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest,
                                                   @PathVariable String userId) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequest, userId));
    }
}
