package com.appointment.auth_service.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.appointment.auth_service.entity.User;
import com.appointment.auth_service.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        if (email == null) {
            response.sendRedirect("http://localhost:4200/auth?oauthError=email_not_found");
            return;
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isEmpty()) {
            user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setProvider("GOOGLE");
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());

            user = userRepository.save(user);
        } else {
            user = existingUser.get();
        }

        if (user.getRole() != null) {
            String token = jwtUtil.generateToken(
                    user.getEmail(),
                    user.getUserId(),
                    user.getRole().name()
            );

            String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

            response.sendRedirect("http://localhost:4200/auth?token=" + encodedToken);
            return;
        }

        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        response.sendRedirect("http://localhost:4200/select-role?email=" + encodedEmail);
    }
}