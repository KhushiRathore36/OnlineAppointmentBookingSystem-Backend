package com.appointment.record_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notifications")
    void sendFollowUpNotification(@RequestBody HashMap<String, Object> request);

	
}