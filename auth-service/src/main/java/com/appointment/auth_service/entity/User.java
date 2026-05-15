package com.appointment.auth_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private boolean isActive;

    private String profilePicUrl;

    private LocalDateTime createdAt;

    // Default Constructor
    public User() {}

    // All Args Constructor
    public User(Long userId, String fullName, String email, String passwordHash,
                String phone, Role role, String provider,
                boolean isActive, String profilePicUrl, LocalDateTime createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.role = role;
        this.provider = provider;
        this.isActive = isActive;
        this.profilePicUrl = profilePicUrl;
        this.createdAt = createdAt;
    }

    // Getters & Setters

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}