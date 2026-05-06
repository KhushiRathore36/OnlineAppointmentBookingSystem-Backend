package com.appointment.auth_service.service;



import com.appointment.auth_service.dto.*;
import com.appointment.auth_service.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
    User getProfile(String email);

    User updateProfile(String email, UpdateProfileRequest request);
    
    void changePassword(String email, ChangePasswordRequest request);
    void deactivateAccount(String email);
    User getUserById(Long userId);
}
