package com.appointment.review_service.dto;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UpdateReviewRequest {

    @Min(1)
    @Max(5)
    private int rating;

    private String comment;
    private boolean anonymous;

    public UpdateReviewRequest() {
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

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}