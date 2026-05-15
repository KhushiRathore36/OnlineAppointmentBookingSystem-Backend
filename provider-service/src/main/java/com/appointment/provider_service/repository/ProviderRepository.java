package com.appointment.provider_service.repository;



import com.appointment.provider_service.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

	List<Provider> findBySpecialization(String specialization);

    List<Provider> findByVerified(boolean verified);   

    List<Provider> findByClinicAddressContaining(String location);

    List<Provider> findByClinicNameContainingIgnoreCase(String name);

    List<Provider> findByAvailable(boolean available);
}
