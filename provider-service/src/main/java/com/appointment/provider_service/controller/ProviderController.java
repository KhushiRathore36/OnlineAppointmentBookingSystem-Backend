package com.appointment.provider_service.controller;

import com.appointment.provider_service.dto.ProviderRequest;
import com.appointment.provider_service.dto.RatingUpdateRequest;
import com.appointment.provider_service.entity.Provider;
import com.appointment.provider_service.service.ProviderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService service;

    // Manual constructor injection
    public ProviderController(ProviderService service) {
        this.service = service;
    }

    // Register Provider
    @PostMapping
    public Provider register(@RequestBody ProviderRequest request) {
        return service.registerProvider(request);
    }

    // Get by ID
    @GetMapping("/{id}")
    public Provider getById(@PathVariable Long id) {
        return service.getProviderById(id);
    }

    // Get by specialization
    @GetMapping("/specialization")
    public List<Provider> getBySpecialization(@RequestParam String specialization) {
        return service.getBySpecialization(specialization);
    }

    // Search by location
    @GetMapping("/search")
    public List<Provider> searchByLocation(@RequestParam String location) {
        return service.searchByLocation(location);
    }

    // Verify provider
    @PutMapping("/{id}/verify")
    public Provider verify(@PathVariable Long id) {
        return service.verifyProvider(id);
    }

    //  Update provider
    @PutMapping("/{id}")
    public Provider update(@PathVariable Long id,
                           @RequestBody ProviderRequest request) {
        return service.updateProvider(id, request);
    }

    // Get all providers
    @GetMapping
    public List<Provider> getAll() {
        return service.getAllProviders();
    }
    @GetMapping("/search/name")
    public List<Provider> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }
    @GetMapping("/verified")
    public List<Provider> getVerified() {
        return service.getVerifiedProviders();
    }
    @GetMapping("/available")
    public List<Provider> getAvailable() {
        return service.getAvailableProviders();
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteProvider(id);
        return "Provider deleted successfully";
    }
    
    @PutMapping("/{id}/availability")
    public Provider updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean isAvailable) {

        return service.updateAvailability(id, isAvailable);
    }
    @PutMapping("/{providerId}/rating")
    public Provider updateRating(@PathVariable Long providerId,
                                 @RequestBody RatingUpdateRequest request) {
        return service.updateRating(providerId, request.getAvgRating());
    }
}