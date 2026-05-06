package com.appointment.review_service.controller;

import com.appointment.review_service.dto.ReviewRequest;
import com.appointment.review_service.dto.UpdateReviewRequest;
import com.appointment.review_service.entity.Review;
import com.appointment.review_service.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    @PostMapping
    public Review addReview(@Valid @RequestBody ReviewRequest request) {
        return service.addReview(request);
    }

    @GetMapping("/provider/{providerId}")
    public List<Review> getByProvider(@PathVariable Long providerId) {
        return service.getByProvider(providerId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Review> getByPatient(@PathVariable Long patientId) {
        return service.getByPatient(patientId);
    }

    @GetMapping("/appointment/{appointmentId}")
    public Review getByAppointment(@PathVariable Long appointmentId) {
        return service.getByAppointment(appointmentId);
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(@PathVariable Long reviewId,
                               @Valid @RequestBody UpdateReviewRequest request) {
        return service.updateReview(reviewId, request);
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        service.deleteReview(reviewId);
        return "Review deleted successfully";
    }

    @GetMapping("/provider/{providerId}/avg")
    public Double getAvgRating(@PathVariable Long providerId) {
        return service.getAvgRating(providerId);
    }

    @GetMapping("/provider/{providerId}/count")
    public long getReviewCount(@PathVariable Long providerId) {
        return service.getReviewCount(providerId);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return service.getAllReviews();
    }
}
