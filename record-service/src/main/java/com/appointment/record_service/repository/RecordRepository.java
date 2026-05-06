package com.appointment.record_service.repository;



import com.appointment.record_service.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<MedicalRecord, Long> {

    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    List<MedicalRecord> findByPatientId(Long patientId);

    List<MedicalRecord> findByProviderId(Long providerId);

    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    List<MedicalRecord> findByFollowUpDate(LocalDate followUpDate);

    long countByPatientId(Long patientId);

    void deleteByRecordId(Long recordId);
}
