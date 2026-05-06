package com.appointment.provider_service.service;



import com.appointment.provider_service.dto.ProviderRequest;
import com.appointment.provider_service.entity.Provider;

import java.util.List;

public interface ProviderService {

    Provider registerProvider(ProviderRequest request);

    Provider getProviderById(Long id);

    List<Provider> getBySpecialization(String specialization);

    List<Provider> searchByLocation(String location);

    Provider verifyProvider(Long id);

    Provider updateProvider(Long id, ProviderRequest request);

    List<Provider> getAllProviders();
    List<Provider> searchByName(String name);
    
    List<Provider> getVerifiedProviders();
    List<Provider> getAvailableProviders();
    void deleteProvider(Long id);
    
    Provider updateAvailability(Long id, boolean available);
    
    Provider updateRating(Long providerId, Double avgRating);
}