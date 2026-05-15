package com.appointment.review_service.entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", uniqueConstraints = @UniqueConstraint(columnNames = "appointmentId"))
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private Long appointmentId;
    private Long patientId;
    private Long providerId;

    private int rating;

    @Column(length = 1000)
    private String comment;

    private LocalDateTime reviewDate;

    private boolean isVerified;
    private boolean isAnonymous;

    public Review() {
    }

    public Review(Long reviewId, Long appointmentId, Long patientId, Long providerId,
                  int rating, String comment, LocalDateTime reviewDate,
                  boolean isVerified, boolean isAnonymous) {
        this.reviewId = reviewId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.providerId = providerId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.isVerified = isVerified;
        this.isAnonymous = isAnonymous;
    }

    @PrePersist
    public void onCreate() {
        this.reviewDate = LocalDateTime.now();
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}