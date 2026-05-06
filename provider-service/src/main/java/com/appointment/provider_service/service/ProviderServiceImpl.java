package com.appointment.provider_service.service;

import com.appointment.provider_service.dto.ProviderRequest;
import com.appointment.provider_service.entity.Provider;
import com.appointment.provider_service.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository repository;

    @Override
    public Provider registerProvider(ProviderRequest request) {

        Provider provider = new Provider();

        provider.setUserId(request.getUserId());
        provider.setSpecialization(request.getSpecialization());
        provider.setQualification(request.getQualification());
        provider.setExperienceYears(request.getExperienceYears());
        provider.setBio(request.getBio());
        provider.setClinicName(request.getClinicName());
        provider.setClinicAddress(request.getClinicAddress());

        provider.setVerified(false);
        provider.setAvailable(true);
        provider.setAvgRating(0.0);
        provider.setCreatedAt(LocalDateTime.now());

        return repository.save(provider);
    }

    @Override
    public Provider getProviderById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    @Override
    public List<Provider> getBySpecialization(String specialization) {
        return repository.findBySpecialization(specialization);
    }

    @Override
    public List<Provider> searchByLocation(String location) {
        return repository.findByClinicAddressContaining(location);
    }

    @Override
    public Provider verifyProvider(Long id) {
        Provider provider = getProviderById(id);
        provider.setVerified(true);
        return repository.save(provider);
    }

    @Override
    public Provider updateProvider(Long id, ProviderRequest request) {
        Provider provider = getProviderById(id);

        provider.setSpecialization(request.getSpecialization());
        provider.setQualification(request.getQualification());
        provider.setExperienceYears(request.getExperienceYears());
        provider.setBio(request.getBio());
        provider.setClinicName(request.getClinicName());
        provider.setClinicAddress(request.getClinicAddress());

        return repository.save(provider);
    }

    @Override
    public List<Provider> getAllProviders() {
        return repository.findAll();
    }
    @Override
    public List<Provider> searchByName(String name) {
        return repository.findByClinicNameContainingIgnoreCase(name);
    }
    
    @Override
    public List<Provider> getVerifiedProviders() {
        return repository.findByVerified(true);  
    }

    @Override
    public List<Provider> getAvailableProviders() {
        return repository.findByAvailable(true); 
    }
    
    @Override
    public void deleteProvider(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public Provider updateAvailability(Long id, boolean isAvailable) {

        Provider provider = getProviderById(id);

        provider.setAvailable(isAvailable);

        return repository.save(provider);
    }
    @Override
    public Provider updateRating(Long providerId, Double avgRating) {
        Provider provider = repository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with id: " + providerId));

        provider.setAvgRating(avgRating);
        return repository.save(provider);
    }
}