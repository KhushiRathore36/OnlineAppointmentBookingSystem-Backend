package com.appointment.schedule_service.service;

import com.appointment.schedule_service.dto.SlotRequest;
import com.appointment.schedule_service.entity.AvailabilitySlot;
import com.appointment.schedule_service.entity.SlotStatus;
import com.appointment.schedule_service.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final SlotRepository repository;

    public ScheduleServiceImpl(SlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AvailabilitySlot> createSlots(SlotRequest request) {

        List<AvailabilitySlot> slots = new ArrayList<>();
        LocalDate currentDate = request.getStartDate();

        while (!currentDate.isAfter(request.getEndDate())) {

            if ("WEEKLY".equalsIgnoreCase(request.getRecurrence())
                    && currentDate.getDayOfWeek() != request.getStartDate().getDayOfWeek()) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            LocalTime time = request.getStartTime();

            while (time.plusMinutes(request.getDurationMinutes()).isBefore(request.getEndTime())
                    || time.plusMinutes(request.getDurationMinutes()).equals(request.getEndTime())) {

                AvailabilitySlot slot = new AvailabilitySlot();
                slot.setProviderId(request.getProviderId());
                slot.setDate(currentDate);
                slot.setStartTime(time);
                slot.setEndTime(time.plusMinutes(request.getDurationMinutes()));
                slot.setDurationMinutes(request.getDurationMinutes());
                slot.setStatus(SlotStatus.AVAILABLE);
                slot.setRecurrence(request.getRecurrence());
                slot.setCreatedAt(LocalDateTime.now());

                slots.add(slot);
                time = time.plusMinutes(request.getDurationMinutes());
            }

            currentDate = currentDate.plusDays(1);
        }

        return repository.saveAll(slots);
    }

    @Override
    public List<AvailabilitySlot> getSlotsByProvider(Long providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public List<AvailabilitySlot> getAvailableSlots(Long providerId, LocalDate date) {
        return repository.findByProviderIdAndDateAndStatus(providerId, date, SlotStatus.AVAILABLE);
    }

    @Override
    public AvailabilitySlot bookSlot(Long slotId) {
        AvailabilitySlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot already booked or blocked");
        }

        slot.setStatus(SlotStatus.BOOKED);
        return repository.save(slot);
    }

    @Override
    public AvailabilitySlot releaseSlot(Long slotId) {
        AvailabilitySlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BLOCKED) {
            throw new RuntimeException("Blocked slot cannot be released directly");
        }

        slot.setStatus(SlotStatus.AVAILABLE);
        return repository.save(slot);
    }

    @Override
    public AvailabilitySlot blockSlot(Long slotId) {
        AvailabilitySlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new RuntimeException("Booked slot cannot be blocked");
        }

        slot.setStatus(SlotStatus.BLOCKED);
        return repository.save(slot);
    }

    @Override
    public AvailabilitySlot getSlotById(Long slotId) {
        return repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + slotId));
    }
}