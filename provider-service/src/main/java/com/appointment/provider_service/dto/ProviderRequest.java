package com.appointment.provider_service.dto;



public class ProviderRequest {

    private Long userId;
    private String specialization;
    private String qualification;
    private int experienceYears;
    private String bio;
    private String clinicName;
    private String clinicAddress;

    // GETTERS

    public Long getUserId() {
        return userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getBio() {
        return bio;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    // SETTERS

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }
}