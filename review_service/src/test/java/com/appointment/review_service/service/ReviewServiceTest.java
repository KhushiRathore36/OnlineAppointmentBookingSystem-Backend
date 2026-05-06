package com.appointment.review_service.service;

import com.appointment.review_service.client.AppointmentServiceClient;
import com.appointment.review_service.client.ProviderServiceClient;
import com.appointment.review_service.dto.AppointmentResponse;
import com.appointment.review_service.dto.ReviewRequest;
import com.appointment.review_service.entity.Review;
import com.appointment.review_service.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private AppointmentServiceClient appointmentServiceClient;

    @Mock
    private ProviderServiceClient providerServiceClient;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void testAddReview_Success() {

        ReviewRequest request = new ReviewRequest();
        request.setAppointmentId(1L);
        request.setPatientId(101L);
        request.setProviderId(201L);
        request.setRating(5);
        request.setComment("Good doctor");
        request.setAnonymous(false);

        // No existing review
        when(repository.existsByAppointmentId(1L)).thenReturn(false);

        // Mock appointment response
        AppointmentResponse appointment = new AppointmentResponse();
        appointment.setAppointmentId(1L);
        appointment.setPatientId(101L);
        appointment.setProviderId(201L);
        appointment.setStatus("COMPLETED");

        when(appointmentServiceClient.getAppointmentById(1L))
                .thenReturn(appointment);

        // Mock save
        Review savedReview = new Review();
        savedReview.setReviewId(1L);
        savedReview.setPatientId(101L);
        savedReview.setProviderId(201L);
        savedReview.setRating(5);
        savedReview.setComment("Good doctor");

        when(repository.save(Mockito.any(Review.class)))
                .thenReturn(savedReview);

        // Mock avg rating
        when(repository.findByProviderId(201L))
                .thenReturn(java.util.List.of(savedReview));

        Review result = reviewService.addReview(request);

        assertNotNull(result);
        assertEquals(5, result.getRating());

        verify(repository, times(1)).save(Mockito.any(Review.class));
        verify(providerServiceClient, times(1))
                .updateRating(eq(201L), anyDouble());
    }
}