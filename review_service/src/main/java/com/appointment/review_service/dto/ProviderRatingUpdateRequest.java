package com.appointment.review_service.dto;



public class ProviderRatingUpdateRequest {

    private Double avgRating;

    public ProviderRatingUpdateRequest() {
    }

    public ProviderRatingUpdateRequest(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
