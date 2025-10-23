package com.learnly.userService.dto;

import com.learnly.userService.model.UserRole;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String  id;
    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private String phoneNumber;

    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
