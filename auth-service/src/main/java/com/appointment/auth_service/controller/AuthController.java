package com.appointment.auth_service.controller;

import com.appointment.auth_service.dto.*;
import com.appointment.auth_service.entity.Role;
import com.appointment.auth_service.entity.User;
import com.appointment.auth_service.repository.UserRepository;
import com.appointment.auth_service.security.JwtUtil;
import com.appointment.auth_service.service.AuthService;
import com.appointment.auth_service.service.TokenBlacklistService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;
   


    public AuthController(AuthService authService,
                          UserRepository userRepository,
                          JwtUtil jwtUtil,TokenBlacklistService tokenBlacklistService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService=tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(authService.getProfile(user.getEmail()));
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(authService.updateProfile(user.getEmail(), request));
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequest request) {

        User user = (User) authentication.getPrincipal();
        authService.changePassword(user.getEmail(), request);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authService.deactivateAccount(user.getEmail());

        return ResponseEntity.ok("Account deactivated successfully");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = authService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/oauth-role")
    public ResponseEntity<AuthResponse> setRole(
            @RequestParam String email,
            @RequestParam String role) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role selectedRole = Role.valueOf(role.toUpperCase());

        user.setRole(selectedRole);
        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getUserId(),
                user.getRole().name()
        );

        return ResponseEntity.ok(new AuthResponse(token, "OAuth login successful"));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        }

        String token = authHeader.substring(7);

        long expiryTime = jwtUtil.getExpirationTime(token);

        tokenBlacklistService.blacklistToken(token, expiryTime);

        return ResponseEntity.ok("Logout successful");
    }
}