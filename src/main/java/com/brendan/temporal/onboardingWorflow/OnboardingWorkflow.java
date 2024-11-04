package com.brendan.temporal.onboardingWorflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface OnboardingWorkflow {
    @WorkflowMethod
    boolean onboard();

    @QueryMethod
    OnboardingState getState();

    //
    //  BASIC INFO
    //
    @UpdateMethod
    CurrentStep captureBasicInfo(String aadhar, String pan, String mobile);

    @UpdateValidatorMethod(updateName = "captureBasicInfo")
    void captureBasicInfoValidator(String aadhar, String pan, String mobile);

    //
    //  AADHAR OTP
    //
    @UpdateMethod
    CurrentStep captureAadharOTP(String aadharOtp);

    @UpdateValidatorMethod(updateName = "captureAadharOTP")
    void captureAadharOTPValidator(String aadharOtp);

    //
    //  USER DETAILS
    //
    @UpdateMethod
    CurrentStep captureUserDetails(String details);

    @UpdateValidatorMethod(updateName = "captureUserDetails")
    void captureUserDetailsValidator(String details);

    //
    //  COMMUNICATION DETAILS
    //
    @UpdateMethod
    CurrentStep captureCommunicationDetails(String emailAddress);

    @UpdateValidatorMethod(updateName = "captureCommunicationDetails")
    void captureCommunicationDetailsValidator(String emailAddress);

    //
    //  EMAIL OTP
    //
    @UpdateMethod
    CurrentStep captureEmailOTP(String emailOtp);

    @UpdateValidatorMethod(updateName = "captureEmailOTP")
    void captureEmailOTPValidator(String emailOtp);

    //
    //  ADDITIONAL DETAILS
    //
    @UpdateMethod
    CurrentStep captureAdditionalDetails(String additionalDetails);

    @UpdateValidatorMethod(updateName = "captureAdditionalDetails")
    void captureAdditionalDetailsValidator(String additionalDetails);
}
