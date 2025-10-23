package com.learnly.userService.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private LocalDate dateOfBirth;

}
