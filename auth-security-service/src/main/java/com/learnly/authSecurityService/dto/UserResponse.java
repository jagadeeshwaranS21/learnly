package com.learnly.authSecurityService.dto;

import lombok.Data;

@Data
public class UserResponse {

    private String email;
    private String password;
    private String userId;
    private String role;
    private boolean active;
}
