package com.learnly.authSecurityService.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
