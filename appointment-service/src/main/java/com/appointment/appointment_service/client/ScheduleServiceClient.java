package com.appointment.appointment_service.client;



import com.appointment.appointment_service.dto.SlotResponse;
import com.appointment.appointment_service.exception.BadRequestException;
import com.appointment.appointment_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduleServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${schedule.service.base-url}")
    private String scheduleServiceBaseUrl;

    public SlotResponse getSlotById(Long slotId) {
        try {
            String url = scheduleServiceBaseUrl + "/slots/" + slotId;
            ResponseEntity<SlotResponse> response = restTemplate.getForEntity(url, SlotResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Slot not found with id: " + slotId);
        } catch (Exception ex) {
            throw new BadRequestException("Unable to fetch slot from schedule-service");
        }
    }

    public void bookSlot(Long slotId) {
        try {
            String url = scheduleServiceBaseUrl + "/slots/" + slotId + "/book";
            restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);
        } catch (Exception ex) {
            throw new BadRequestException("Unable to book slot with id: " + slotId);
        }
    }

    public void releaseSlot(Long slotId) {
        try {
            String url = scheduleServiceBaseUrl + "/slots/" + slotId + "/release";
            restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);
        } catch (Exception ex) {
            throw new BadRequestException("Unable to release slot with id: " + slotId);
        }
    }
}
