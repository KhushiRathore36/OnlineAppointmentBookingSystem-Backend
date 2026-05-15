package com.appointment.schedule_service.service;



import com.appointment.schedule_service.dto.SlotRequest;
import com.appointment.schedule_service.entity.AvailabilitySlot;
import com.appointment.schedule_service.repository.SlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private SlotRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void testCreateSlots_Success() {
        SlotRequest request = new SlotRequest();
        request.setProviderId(1L);
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now());
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(11, 0));
        request.setDurationMinutes(30);
        request.setRecurrence("NONE");

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setProviderId(1L);
        slot.setDate(LocalDate.now());
        slot.setStartTime(LocalTime.of(10, 0));
        slot.setEndTime(LocalTime.of(10, 30));

        when(scheduleRepository.save(Mockito.any(AvailabilitySlot.class))).thenReturn(slot);

        List<AvailabilitySlot> result = scheduleService.createSlots(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(scheduleRepository, atLeastOnce()).save(Mockito.any(AvailabilitySlot.class));
    }
}