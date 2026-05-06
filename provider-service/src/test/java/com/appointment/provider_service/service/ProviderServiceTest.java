package com.appointment.provider_service.service;

import com.appointment.provider_service.dto.ProviderRequest;
import com.appointment.provider_service.entity.Provider;
import com.appointment.provider_service.repository.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository repository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Test
    void testRegisterProvider_Success() {
        ProviderRequest request = new ProviderRequest();
        request.setUserId(101L);
        request.setSpecialization("Cardiology");
        request.setQualification("MBBS");
        request.setExperienceYears(5);
        request.setBio("Heart specialist");
        request.setClinicName("Sharma Clinic");
        request.setClinicAddress("Agra");

        Provider savedProvider = new Provider();
        savedProvider.setProviderId(1L);
        savedProvider.setUserId(101L);
        savedProvider.setSpecialization("Cardiology");
        savedProvider.setQualification("MBBS");
        savedProvider.setExperienceYears(5);
        savedProvider.setBio("Heart specialist");
        savedProvider.setClinicName("Sharma Clinic");
        savedProvider.setClinicAddress("Agra");
        savedProvider.setVerified(false);
        savedProvider.setAvailable(true);
        savedProvider.setAvgRating(0.0);

        when(repository.save(Mockito.any(Provider.class)))
                .thenReturn(savedProvider);

        Provider result = providerService.registerProvider(request);

        assertNotNull(result);
        assertEquals(101L, result.getUserId());
        assertEquals("Cardiology", result.getSpecialization());
        assertEquals("Sharma Clinic", result.getClinicName());
        assertFalse(result.isVerified());
        assertTrue(result.isAvailable());
        assertEquals(0.0, result.getAvgRating());

        verify(repository, times(1)).save(Mockito.any(Provider.class));
    }
}