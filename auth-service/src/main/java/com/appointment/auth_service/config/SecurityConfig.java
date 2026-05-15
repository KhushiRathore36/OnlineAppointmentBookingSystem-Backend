package com.appointment.auth_service.config;

import com.appointment.auth_service.security.JwtFilter;
import com.appointment.auth_service.security.OAuth2SuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter,
                          OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/register",
                    "/auth/login",
                    "/auth/oauth-role",
                    "/oauth2/**",
                    "/login/oauth2/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .oauth2Login(oauth -> oauth
                .successHandler(oAuth2SuccessHandler)
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}