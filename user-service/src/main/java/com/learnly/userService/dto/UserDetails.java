package com.learnly.userService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails {

    private String email;
    private String password;
    private String userId;
    private String role;
    private boolean active;
}
