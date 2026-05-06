package com.appointment.review_service.repository;



import com.appointment.review_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProviderId(Long providerId);

    List<Review> findByPatientId(Long patientId);

    Optional<Review> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    long countByProviderId(Long providerId);
}
