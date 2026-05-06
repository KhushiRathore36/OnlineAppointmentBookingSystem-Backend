package com.appointment.record_service.controller;

import com.appointment.record_service.dto.AttachmentRequest;
import com.appointment.record_service.dto.MedicalRecordRequest;
import com.appointment.record_service.entity.MedicalRecord;
import com.appointment.record_service.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.createRecord(request));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long recordId) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(recordId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecord> getRecordByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(medicalRecordService.getRecordByAppointment(appointmentId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByPatient(patientId));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByProvider(providerId));
    }

    @GetMapping("/followups")
    public ResponseEntity<List<MedicalRecord>> getFollowUpRecords(@RequestParam String date) {
        return ResponseEntity.ok(medicalRecordService.getFollowUpRecords(LocalDate.parse(date)));
    }

    @GetMapping("/count/patient/{patientId}")
    public ResponseEntity<Long> getRecordCount(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordCount(patientId));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<MedicalRecord> updateRecord(@PathVariable Long recordId,
                                                      @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(medicalRecordService.updateRecord(recordId, request));
    }

    @PutMapping("/{recordId}/attach-document")
    public ResponseEntity<MedicalRecord> attachDocument(@PathVariable Long recordId,
                                                        @Valid @RequestBody AttachmentRequest request) {
        return ResponseEntity.ok(medicalRecordService.attachDocument(recordId, request.getAttachmentUrl()));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId) {
        medicalRecordService.deleteRecord(recordId);
        return ResponseEntity.ok("Medical record deleted successfully");
    }
}