package com.appointment.review_service.service;

import com.appointment.review_service.dto.ReviewRequest;
import com.appointment.review_service.dto.UpdateReviewRequest;
import com.appointment.review_service.entity.Review;

import java.util.List;

public interface ReviewService {

    Review addReview(ReviewRequest request);

    List<Review> getByProvider(Long providerId);

    List<Review> getByPatient(Long patientId);

    Review getByAppointment(Long appointmentId);

    Review updateReview(Long reviewId, UpdateReviewRequest request);

    void deleteReview(Long reviewId);

    Double getAvgRating(Long providerId);

    long getReviewCount(Long providerId);

    List<Review> getAllReviews();
}
