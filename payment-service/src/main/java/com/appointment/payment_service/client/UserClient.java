package com.appointment.payment_service.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/users/{userId}")
    Map<String, Object> getUserById(@PathVariable Long userId);
}