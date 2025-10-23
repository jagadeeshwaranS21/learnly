package com.learnly.authSecurityService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    @Column(nullable = false, unique = true)
    private String token;
    private Instant expiryDate;
    private boolean revoked;
}
