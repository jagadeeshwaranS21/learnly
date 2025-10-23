package com.learnly.userService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "First name is mandatory")
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;

}
