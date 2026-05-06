package com.appointment.appointment_service.service;

import com.appointment.appointment_service.client.NotificationClient;
import com.appointment.appointment_service.client.ScheduleServiceClient;
import com.appointment.appointment_service.client.UserClient;
import com.appointment.appointment_service.dto.AppointmentRequest;
import com.appointment.appointment_service.dto.NotificationMessage;
import com.appointment.appointment_service.dto.SlotResponse;
import com.appointment.appointment_service.entity.Appointment;
import com.appointment.appointment_service.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private ScheduleServiceClient scheduleServiceClient;

    @Mock
    private NotificationProducer notificationProducer;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void testBookAppointment_Success() {
        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(101L);
        request.setProviderId(201L);
        request.setSlotId(301L);
        request.setServiceType("Consultation");
        request.setNotes("Fever");
        request.setModeOfConsultation("ONLINE");

        when(appointmentRepository.findBySlotId(301L))
                .thenReturn(Optional.empty());

        SlotResponse slot = new SlotResponse();
        slot.setProviderId(201L);
        slot.setStatus("AVAILABLE");
        slot.setDate(LocalDate.now());
        slot.setStartTime(LocalTime.of(10, 0));
        slot.setEndTime(LocalTime.of(10, 30));

        when(scheduleServiceClient.getSlotById(301L))
                .thenReturn(slot);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setAppointmentId(1L);
        savedAppointment.setPatientId(101L);
        savedAppointment.setProviderId(201L);
        savedAppointment.setSlotId(301L);
        savedAppointment.setServiceType("Consultation");
        savedAppointment.setAppointmentDate(slot.getDate());
        savedAppointment.setStartTime(slot.getStartTime());
        savedAppointment.setEndTime(slot.getEndTime());

        when(appointmentRepository.save(Mockito.any(Appointment.class)))
                .thenReturn(savedAppointment);

        Map<String, Object> user = new HashMap<>();
        user.put("email", "test@gmail.com");

        when(userClient.getUserById(101L))
                .thenReturn(user);

        Appointment result = appointmentService.bookAppointment(request);

        assertNotNull(result);
        assertEquals(101L, result.getPatientId());
        assertEquals(201L, result.getProviderId());

        verify(scheduleServiceClient, times(1)).getSlotById(301L);
        verify(scheduleServiceClient, times(1)).bookSlot(301L);
        verify(appointmentRepository, times(1)).save(Mockito.any(Appointment.class));
        verify(userClient, times(1)).getUserById(101L);
        verify(notificationProducer, times(1))
                .sendNotification(Mockito.any(NotificationMessage.class));
    }
}