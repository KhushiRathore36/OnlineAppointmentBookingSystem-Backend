package com.appointment.record_service.service;



import com.appointment.record_service.dto.MedicalRecordRequest;
import com.appointment.record_service.entity.MedicalRecord;
import com.appointment.record_service.repository.RecordRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

    @Mock
    private RecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordServiceImpl recordService;

    @Test
    void testCreateMedicalRecord_Success() {

        MedicalRecordRequest request = new MedicalRecordRequest();
        request.setPatientId(101L);
        request.setProviderId(201L);
        request.setAppointmentId(301L);
        request.setDiagnosis("Fever");
        request.setPrescription("Paracetamol");
        request.setNotes("Take rest");

        MedicalRecord savedRecord = new MedicalRecord();
        savedRecord.setRecordId(1L);
        savedRecord.setPatientId(101L);
        savedRecord.setProviderId(201L);
        savedRecord.setAppointmentId(301L);
        savedRecord.setDiagnosis("Fever");
        savedRecord.setPrescription("Paracetamol");
        savedRecord.setNotes("Take rest");

        when(medicalRecordRepository.save(Mockito.any(MedicalRecord.class)))
                .thenReturn(savedRecord);

        MedicalRecord result = recordService.createRecord(request);

        assertNotNull(result);
        assertEquals(101L, result.getPatientId());
        assertEquals("Fever", result.getDiagnosis());
        assertEquals("Paracetamol", result.getPrescription());

        verify(medicalRecordRepository, times(1))
                .save(Mockito.any(MedicalRecord.class));
    }
}