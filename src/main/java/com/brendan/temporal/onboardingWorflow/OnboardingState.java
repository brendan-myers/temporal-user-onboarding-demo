package com.brendan.temporal.onboardingWorflow;

public class OnboardingState {
    private String aadhar, pan, mobile, details, emailAddress, additionalDetails;
    private CurrentStep step;

    public OnboardingState() {
        // BASIC INFO
        this.aadhar = "";
        this.pan = "";
        this.mobile = "";
        // USER DETAILS
        this.details = "";
        // COMMUNICATION DETAILS
        this.emailAddress = "";
        // ADDITIONAL DETAILS
        this.additionalDetails = "";

        this.step = CurrentStep.WAITING_FOR_BASIC_INFO;
    }

    public String getAadhar() {
        return this.aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return this.pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAdditionalDetails() {
        return this.additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public CurrentStep getStep() {
        return this.step;
    }

    public void setStep(CurrentStep step) {
        this.step = step;
    }
}
