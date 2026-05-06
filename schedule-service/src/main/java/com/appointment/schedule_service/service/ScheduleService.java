package com.appointment.schedule_service.service;



import com.appointment.schedule_service.dto.SlotRequest;
import com.appointment.schedule_service.entity.AvailabilitySlot;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    List<AvailabilitySlot> createSlots(SlotRequest request);

    List<AvailabilitySlot> getSlotsByProvider(Long providerId);

    List<AvailabilitySlot> getAvailableSlots(Long providerId, LocalDate date);

    AvailabilitySlot bookSlot(Long slotId);

    AvailabilitySlot releaseSlot(Long slotId);

    AvailabilitySlot blockSlot(Long slotId);

    AvailabilitySlot getSlotById(Long slotId);
}
