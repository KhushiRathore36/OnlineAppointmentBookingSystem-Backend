package com.appointment.review_service.client;

import com.appointment.review_service.dto.AppointmentResponse;
import com.appointment.review_service.exception.BadRequestException;
import com.appointment.review_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AppointmentServiceClient {

    private final RestTemplate restTemplate;

    @Value("${appointment.service.base-url}")
    private String appointmentServiceBaseUrl;

    public AppointmentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AppointmentResponse getAppointmentById(Long appointmentId) {
        try {
            String url = appointmentServiceBaseUrl + "/appointments/" + appointmentId;
            ResponseEntity<AppointmentResponse> response =
                    restTemplate.getForEntity(url, AppointmentResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        } catch (Exception ex) {
            throw new BadRequestException("Unable to fetch appointment from appointment-service");
        }
    }
}