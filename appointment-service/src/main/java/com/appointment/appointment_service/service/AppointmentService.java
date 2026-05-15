package com.appointment.appointment_service.service;



import com.appointment.appointment_service.dto.AppointmentRequest;
import com.appointment.appointment_service.dto.RescheduleRequest;
import com.appointment.appointment_service.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment bookAppointment(AppointmentRequest request);

    Appointment getById(Long id);

    List<Appointment> getByPatient(Long patientId);

    List<Appointment> getByProvider(Long providerId);

    List<Appointment> getByProviderAndDate(Long providerId, LocalDate date);

    Appointment cancelAppointment(Long id);

    Appointment rescheduleAppointment(Long id, RescheduleRequest request);

    Appointment completeAppointment(Long id);

    Appointment updateStatus(Long id, String status);

    List<Appointment> getUpcomingByPatient(Long patientId);

    long getAppointmentCount(Long providerId);
}
