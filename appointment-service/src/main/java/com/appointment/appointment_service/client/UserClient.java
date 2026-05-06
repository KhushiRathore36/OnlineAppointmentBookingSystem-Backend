package com.appointment.appointment_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.appointment.appointment_service.config.FeignConfig;

import java.util.Map;

@FeignClient(name = "auth-service" , configuration = FeignConfig.class)
public interface UserClient {

    @GetMapping("/auth/users/{userId}")
    Map<String, Object> getUserById(@PathVariable Long userId);
}

