package com.appointment.appointment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "schedule-service")
public interface ScheduleClient {

	@PutMapping("/slots/{id}/book")
    void bookSlot(@PathVariable Long id);

    @PutMapping("/slots/{id}/release")
    void releaseSlot(@PathVariable Long id);
}
