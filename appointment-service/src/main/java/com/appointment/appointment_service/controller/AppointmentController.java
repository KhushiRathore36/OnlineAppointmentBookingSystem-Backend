package com.appointment.appointment_service.controller;

import com.appointment.appointment_service.dto.AppointmentRequest;
import com.appointment.appointment_service.dto.RescheduleRequest;
import com.appointment.appointment_service.dto.StatusUpdateRequest;
import com.appointment.appointment_service.entity.Appointment;
import com.appointment.appointment_service.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public Appointment bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        return appointmentService.bookAppointment(request);
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.getById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getByPatient(patientId);
    }

    @GetMapping("/provider/{providerId}")
    public List<Appointment> getByProvider(@PathVariable Long providerId) {
        return appointmentService.getByProvider(providerId);
    }

    @GetMapping("/provider/{providerId}/date/{date}")
    public List<Appointment> getByProviderAndDate(@PathVariable Long providerId,
                                                  @PathVariable LocalDate date) {
        return appointmentService.getByProviderAndDate(providerId, date);
    }

    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable Long id) {
        return appointmentService.cancelAppointment(id);
    }

    @PutMapping("/{id}/reschedule")
    public Appointment rescheduleAppointment(@PathVariable Long id,
                                             @Valid @RequestBody RescheduleRequest request) {
        return appointmentService.rescheduleAppointment(id, request);
    }

    @PutMapping("/{id}/complete")
    public Appointment completeAppointment(@PathVariable Long id) {
        return appointmentService.completeAppointment(id);
    }

    @PutMapping("/{id}/status")
    public Appointment updateStatus(@PathVariable Long id,
                                    @Valid @RequestBody StatusUpdateRequest request) {
        return appointmentService.updateStatus(id, request.getStatus());
    }

    @GetMapping("/patient/{patientId}/upcoming")
    public List<Appointment> getUpcomingByPatient(@PathVariable Long patientId) {
        return appointmentService.getUpcomingByPatient(patientId);
    }

    @GetMapping("/count/provider/{providerId}")
    public long getAppointmentCount(@PathVariable Long providerId) {
        return appointmentService.getAppointmentCount(providerId);
    }
    
}
