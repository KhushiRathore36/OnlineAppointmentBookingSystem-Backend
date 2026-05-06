package com.appointment.schedule_service.controller;

import com.appointment.schedule_service.dto.SlotRequest;
import com.appointment.schedule_service.entity.AvailabilitySlot;
import com.appointment.schedule_service.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/slots")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @PostMapping
    public List<AvailabilitySlot> create(@RequestBody SlotRequest request) {
        return service.createSlots(request);
    }

    @GetMapping("/provider/{providerId}")
    public List<AvailabilitySlot> getByProvider(@PathVariable Long providerId) {
        return service.getSlotsByProvider(providerId);
    }

    @GetMapping("/available")
    public List<AvailabilitySlot> getAvailable(@RequestParam Long providerId,
                                               @RequestParam String date) {
        return service.getAvailableSlots(providerId, LocalDate.parse(date));
    }

    @PutMapping("/{id}/book")
    public AvailabilitySlot book(@PathVariable Long id) {
        return service.bookSlot(id);
    }

    @PutMapping("/{id}/release")
    public AvailabilitySlot release(@PathVariable Long id) {
        return service.releaseSlot(id);
    }

    @PutMapping("/{id}/block")
    public AvailabilitySlot block(@PathVariable Long id) {
        return service.blockSlot(id);
    }

    @GetMapping("/{id}")
    public AvailabilitySlot getById(@PathVariable Long id) {
        return service.getSlotById(id);
    }
}