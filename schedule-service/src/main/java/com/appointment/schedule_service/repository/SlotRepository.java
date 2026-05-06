package com.appointment.schedule_service.repository;



import com.appointment.schedule_service.entity.AvailabilitySlot;
import com.appointment.schedule_service.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByProviderId(Long providerId);

    List<AvailabilitySlot> findByProviderIdAndDate(Long providerId, LocalDate date);

    List<AvailabilitySlot> findByProviderIdAndDateAndStatus(
            Long providerId, LocalDate date, SlotStatus status
    );
}
