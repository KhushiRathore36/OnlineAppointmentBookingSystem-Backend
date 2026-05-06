package com.appointment.record_service.client;

import com.appointment.record_service.dto.AppointmentResponse;
import com.appointment.record_service.exception.BadRequestException;
import com.appointment.record_service.exception.ResourceNotFoundException;
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

            AppointmentResponse body = response.getBody();

            if (body == null) {
                throw new ResourceNotFoundException("Appointment response is empty for id: " + appointmentId);
            }

            return body;

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
        } catch (HttpClientErrorException ex) {
            throw new BadRequestException(
                    "Error from appointment-service: " + ex.getStatusCode());
        } catch (Exception ex) {
            throw new BadRequestException(
                    "Unable to fetch appointment from appointment-service: " + ex.getMessage());
        }
    }
}