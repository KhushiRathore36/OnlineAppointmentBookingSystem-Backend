package com.appointment.auth_service.service;

import com.appointment.auth_service.dto.AuthResponse;
import com.appointment.auth_service.dto.RegisterRequest;
import com.appointment.auth_service.entity.Role;
import com.appointment.auth_service.entity.User;
import com.appointment.auth_service.repository.UserRepository;
import com.appointment.auth_service.security.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegisterUser_Success() {

        RegisterRequest request = new RegisterRequest();
        request.setFullName("Khushi Rathore");
        request.setEmail("khushi@gmail.com");
        request.setPassword("123456");
        request.setPhone("9876543210");
        request.setRole("PATIENT");

        when(userRepository.existsByEmail("khushi@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

       
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setUserId(1L);
                    return user;
                });

        when(jwtUtil.generateToken("khushi@gmail.com", 1L, "PATIENT"))
                .thenReturn("dummy-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("dummy-token", response.getToken());
        assertEquals("User registered successfully", response.getMessage());

        verify(userRepository, times(1)).existsByEmail("khushi@gmail.com");
        verify(passwordEncoder, times(1)).encode("123456");
        verify(userRepository, times(1)).save(Mockito.any(User.class));
        verify(jwtUtil, times(1))
                .generateToken("khushi@gmail.com", 1L, "PATIENT");
    }
}