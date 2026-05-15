package com.appointment.provider_service.dto;

public class RatingUpdateRequest {

    private Double avgRating;

    public RatingUpdateRequest() {
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
