package com.appointment.record_service.service;

import com.appointment.record_service.client.AppointmentServiceClient;
import com.appointment.record_service.client.NotificationClient;
import com.appointment.record_service.dto.AppointmentResponse;
import com.appointment.record_service.dto.MedicalRecordRequest;
import com.appointment.record_service.entity.MedicalRecord;
import com.appointment.record_service.exception.BadRequestException;
import com.appointment.record_service.exception.ResourceNotFoundException;
import com.appointment.record_service.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final RecordRepository recordRepository;
    private final NotificationClient notificationClient;
    private final AppointmentServiceClient appointmentServiceClient;

    public MedicalRecordServiceImpl(RecordRepository recordRepository,
                                    NotificationClient notificationClient,
                                    AppointmentServiceClient appointmentServiceClient) {
        this.recordRepository = recordRepository;
        this.notificationClient = notificationClient;
        this.appointmentServiceClient = appointmentServiceClient;
    }

    @Override
    public MedicalRecord createRecord(MedicalRecordRequest request) {

        if (recordRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new BadRequestException(
                    "Medical record already exists for appointment id: " + request.getAppointmentId());
        }

        AppointmentResponse appointment =
                appointmentServiceClient.getAppointmentById(request.getAppointmentId());

        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found");
        }

        if (appointment.getStatus() == null ||
                !appointment.getStatus().equalsIgnoreCase("COMPLETED")) {
            throw new BadRequestException("Record can only be created for completed appointments");
        }

        if (!appointment.getPatientId().equals(request.getPatientId())) {
            throw new BadRequestException("Patient does not match appointment");
        }

        if (!appointment.getProviderId().equals(request.getProviderId())) {
            throw new BadRequestException("Provider does not match appointment");
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointmentId(request.getAppointmentId());
        record.setPatientId(request.getPatientId());
        record.setProviderId(request.getProviderId());
        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());
        record.setAttachmentUrl(request.getAttachmentUrl());
        record.setFollowUpDate(request.getFollowUpDate());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        MedicalRecord savedRecord = recordRepository.save(record);

        if (savedRecord.getFollowUpDate() != null) {
            HashMap<String, Object> notification = new HashMap<>();
            notification.put("recipientId", savedRecord.getPatientId());
            notification.put("type", "FOLLOWUP");
            notification.put("title", "Follow-up Reminder");
            notification.put("message", "You have a follow-up on " + savedRecord.getFollowUpDate());
            notification.put("channel", "APP");
            notification.put("relatedId", savedRecord.getRecordId());
            notification.put("relatedType", "RECORD");

            notificationClient.sendFollowUpNotification(notification);
        }

        return savedRecord;
    }

    @Override
    public MedicalRecord getRecordByAppointment(Long appointmentId) {
        return recordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found for appointment id: " + appointmentId));
    }

    @Override
    public List<MedicalRecord> getRecordsByPatient(Long patientId) {
        return recordRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    public List<MedicalRecord> getRecordsByProvider(Long providerId) {
        return recordRepository.findByProviderId(providerId);
    }

    @Override
    public MedicalRecord updateRecord(Long recordId, MedicalRecordRequest request) {
        MedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));

        record.setDiagnosis(request.getDiagnosis());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());
        record.setAttachmentUrl(request.getAttachmentUrl());
        record.setFollowUpDate(request.getFollowUpDate());
        record.setUpdatedAt(LocalDateTime.now());

        MedicalRecord updatedRecord = recordRepository.save(record);

        if (updatedRecord.getFollowUpDate() != null) {
            HashMap<String, Object> notification = new HashMap<>();
            notification.put("recipientId", updatedRecord.getPatientId());
            notification.put("type", "FOLLOWUP");
            notification.put("title", "Follow-up Reminder Updated");
            notification.put("message", "Your updated follow-up date is " + updatedRecord.getFollowUpDate());
            notification.put("channel", "APP");
            notification.put("relatedId", updatedRecord.getRecordId());
            notification.put("relatedType", "RECORD");

            notificationClient.sendFollowUpNotification(notification);
        }

        return updatedRecord;
    }

    @Override
    public void deleteRecord(Long recordId) {
        MedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));

        recordRepository.delete(record);
    }

    @Override
    public MedicalRecord getRecordById(Long recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));
    }

    @Override
    public List<MedicalRecord> getFollowUpRecords(LocalDate followUpDate) {
        return recordRepository.findByFollowUpDate(followUpDate);
    }

    @Override
    public long getRecordCount(Long patientId) {
        return recordRepository.countByPatientId(patientId);
    }

    @Override
    public MedicalRecord attachDocument(Long recordId, String attachmentUrl) {
        MedicalRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));

        record.setAttachmentUrl(attachmentUrl);
        record.setUpdatedAt(LocalDateTime.now());

        return recordRepository.save(record);
    }

    @Override
    public List<MedicalRecord> getAll() {
        return recordRepository.findAll();
    }
}