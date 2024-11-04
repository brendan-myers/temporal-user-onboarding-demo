package com.brendan.temporal.onboardingWorflow;

public enum CurrentStep {
    WAITING_FOR_BASIC_INFO,
    WAITING_FOR_AADHAR_OTP,
    VERIFYING_AADHAR_OTP,
    RETRY_WAITING_FOR_AADHAR_OTP,
    WAITING_FOR_USER_DETAILS,
    WAITING_FOR_COMMUNICATION_DETAILS,
    WAITING_FOR_EMAIL_OTP,
    VERIFYING_EMAIL_OTP,
    RETRY_WAITING_FOR_EMAIL_OTP,
    WAITING_FOR_ADDITIONAL_DETAILS,
    COMPLETE
}
