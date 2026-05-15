package com.appointment.appointment_service.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "provider-service")
public interface ProviderClient {

    @GetMapping("/providers/{providerId}")
    Map<String, Object> getProviderById(@PathVariable Long providerId);
}