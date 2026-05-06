package com.appointment.appointment_service.repository;



import com.appointment.appointment_service.entity.Appointment;
import com.appointment.appointment_service.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByProviderId(Long providerId);

    Optional<Appointment> findBySlotId(Long slotId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByProviderIdAndAppointmentDate(Long providerId, LocalDate appointmentDate);

    List<Appointment> findByPatientIdAndAppointmentDateAfter(Long patientId, LocalDate date);

    long countByProviderId(Long providerId);
}
