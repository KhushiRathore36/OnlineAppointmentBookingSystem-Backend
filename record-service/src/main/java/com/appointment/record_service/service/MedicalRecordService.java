package com.appointment.record_service.service;



import com.appointment.record_service.dto.MedicalRecordRequest;
import com.appointment.record_service.entity.MedicalRecord;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordService {

    MedicalRecord createRecord(MedicalRecordRequest request);

    MedicalRecord getRecordByAppointment(Long appointmentId);

    List<MedicalRecord> getRecordsByPatient(Long patientId);

    List<MedicalRecord> getRecordsByProvider(Long providerId);

    MedicalRecord updateRecord(Long recordId, MedicalRecordRequest request);

    void deleteRecord(Long recordId);

    MedicalRecord getRecordById(Long recordId);

    List<MedicalRecord> getFollowUpRecords(LocalDate followUpDate);

    long getRecordCount(Long patientId);

    MedicalRecord attachDocument(Long recordId, String attachmentUrl);

    List<MedicalRecord> getAll();
}
