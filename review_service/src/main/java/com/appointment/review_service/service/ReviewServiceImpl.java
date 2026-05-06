package com.appointment.review_service.service;

import com.appointment.review_service.client.AppointmentServiceClient;
import com.appointment.review_service.client.ProviderServiceClient;
import com.appointment.review_service.dto.AppointmentResponse;
import com.appointment.review_service.dto.ReviewRequest;
import com.appointment.review_service.dto.UpdateReviewRequest;
import com.appointment.review_service.entity.Review;
import com.appointment.review_service.exception.BadRequestException;
import com.appointment.review_service.exception.ResourceNotFoundException;
import com.appointment.review_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final AppointmentServiceClient appointmentServiceClient;
    private final ProviderServiceClient providerServiceClient;

    public ReviewServiceImpl(ReviewRepository repository,
                             AppointmentServiceClient appointmentServiceClient,
                             ProviderServiceClient providerServiceClient) {
        this.repository = repository;
        this.appointmentServiceClient = appointmentServiceClient;
        this.providerServiceClient = providerServiceClient;
    }

    @Override
    public Review addReview(ReviewRequest request) {

        if (repository.existsByAppointmentId(request.getAppointmentId())) {
            throw new BadRequestException("Review already exists for this appointment");
        }

        AppointmentResponse appointment =
                appointmentServiceClient.getAppointmentById(request.getAppointmentId());

        if (appointment == null) {
            throw new ResourceNotFoundException("Appointment not found");
        }

        if (!"COMPLETED".equalsIgnoreCase(appointment.getStatus())) {
            throw new BadRequestException("Review can only be added for completed appointments");
        }

        if (!appointment.getPatientId().equals(request.getPatientId())) {
            throw new BadRequestException("Patient does not match appointment");
        }

        if (!appointment.getProviderId().equals(request.getProviderId())) {
            throw new BadRequestException("Provider does not match appointment");
        }

        Review review = new Review();
        review.setAppointmentId(request.getAppointmentId());
        review.setPatientId(request.getPatientId());
        review.setProviderId(request.getProviderId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setAnonymous(request.isAnonymous());
        review.setVerified(true);

        Review savedReview = repository.save(review);

        Double avgRating = getAvgRating(request.getProviderId());
        providerServiceClient.updateRating(request.getProviderId(), avgRating);

        return savedReview;
    }

    @Override
    public List<Review> getByProvider(Long providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public List<Review> getByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    @Override
    public Review getByAppointment(Long appointmentId) {
        return repository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found for appointment id: " + appointmentId));
    }

    @Override
    public Review updateReview(Long reviewId, UpdateReviewRequest request) {
        Review review = repository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId));

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setAnonymous(request.isAnonymous());

        Review updatedReview = repository.save(review);

        Double avgRating = getAvgRating(review.getProviderId());
        providerServiceClient.updateRating(review.getProviderId(), avgRating);

        return updatedReview;
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review review = repository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + reviewId));

        Long providerId = review.getProviderId();
        repository.delete(review);

        Double avgRating = getAvgRating(providerId);
        providerServiceClient.updateRating(providerId, avgRating);
    }

    @Override
    public Double getAvgRating(Long providerId) {
        List<Review> reviews = repository.findByProviderId(providerId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        for (Review review : reviews) {
            total += review.getRating();
        }

        return total / reviews.size();
    }

    @Override
    public long getReviewCount(Long providerId) {
        return repository.countByProviderId(providerId);
    }

    @Override
    public List<Review> getAllReviews() {
        return repository.findAll();
    }
}
