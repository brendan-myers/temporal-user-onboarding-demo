package com.brendan.temporal.onboardingWorflow;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.UpdateValidatorMethod;
import io.temporal.workflow.Workflow;

@WorkflowImpl(taskQueues = "onboarding-queue")
public class OnboardingWorkflowImpl implements OnboardingWorkflow {

    private OnboardingActivity activity = Workflow.newActivityStub(
        OnboardingActivity.class,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(20))
            .build()
    );

    OnboardingState state = new OnboardingState();

    @Override
    public OnboardingState getState() {
        return state;
    }
 

    //
    // BASIC INFO
    //
    @Override
    public CurrentStep captureBasicInfo(String aadhar, String pan, String mobile) {
        state.setAadhar(aadhar);
        state.setPan(pan);
        state.setMobile(mobile);
        state.setStep(CurrentStep.WAITING_FOR_AADHAR_OTP);

        return state.getStep();
    }

    @Override
    public void captureBasicInfoValidator(String aadhar, String pan, String mobile) {
        if (state.getStep() != CurrentStep.WAITING_FOR_BASIC_INFO) {
            throw new IllegalArgumentException();
        }

        if (aadhar.isBlank() || pan.isBlank() || mobile.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }


    //
    // AADHAR OTP
    //
    @Override
    public CurrentStep captureAadharOTP(String aadharOtp) {
        state.setStep(CurrentStep.VERIFYING_AADHAR_OTP);

        if (activity.verifyAadharOtp(aadharOtp)) {
            // successful validation
            state.setStep(CurrentStep.WAITING_FOR_USER_DETAILS);
        } else {
            // failed validation
            state.setStep(CurrentStep.RETRY_WAITING_FOR_AADHAR_OTP);
        }

        return state.getStep();
    }

    @Override
    public void captureAadharOTPValidator(String aadharOtp) {
        if (state.getStep() != CurrentStep.WAITING_FOR_AADHAR_OTP && state.getStep() != CurrentStep.RETRY_WAITING_FOR_AADHAR_OTP) {
            throw new IllegalArgumentException();
        }

        if (aadharOtp.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }


    //
    //  USER DETAILS
    //
    @UpdateMethod
    public CurrentStep captureUserDetails(String details) {
        state.setDetails(details);
        state.setStep(CurrentStep.WAITING_FOR_COMMUNICATION_DETAILS);

        return state.getStep();
    }

    @UpdateValidatorMethod(updateName = "captureUserDetails")
    public void captureUserDetailsValidator(String details) {
        if (state.getStep() != CurrentStep.WAITING_FOR_USER_DETAILS) {
            throw new IllegalArgumentException();
        }

        if (details.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }

    //
    //  COMMUNICATION DETAILS
    //
    @UpdateMethod
    public CurrentStep captureCommunicationDetails(String emailAddress) {
        state.setEmailAddress(emailAddress);
        state.setStep(CurrentStep.WAITING_FOR_EMAIL_OTP);

        return state.getStep();
    }

    @UpdateValidatorMethod(updateName = "captureCommunicationDetails")
    public void captureCommunicationDetailsValidator(String emailAddress) {
        if (state.getStep() != CurrentStep.WAITING_FOR_COMMUNICATION_DETAILS) {
            throw new IllegalArgumentException();
        }

        if (emailAddress.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }

    //
    //  EMAIL OTP
    //
    @UpdateMethod
    public CurrentStep captureEmailOTP(String emailOtp) {
        state.setStep(CurrentStep.VERIFYING_EMAIL_OTP);

        if (activity.verifyEmailOtp(emailOtp)) {
            // successful validation
            state.setStep(CurrentStep.WAITING_FOR_ADDITIONAL_DETAILS);
        } else {
            // failed validation
            state.setStep(CurrentStep.RETRY_WAITING_FOR_EMAIL_OTP);
        }

        return state.getStep();
    }

    @UpdateValidatorMethod(updateName = "captureEmailOTP")
    public void captureEmailOTPValidator(String emailOtp) {
        if (state.getStep() != CurrentStep.WAITING_FOR_EMAIL_OTP && state.getStep() != CurrentStep.RETRY_WAITING_FOR_EMAIL_OTP) {
            throw new IllegalArgumentException();
        }

        if (emailOtp.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }

    //
    //  ADDITIONAL DETAILS
    //
    @UpdateMethod
    public CurrentStep captureAdditionalDetails(String additionalDetails) {
        state.setAdditionalDetails(additionalDetails);
        state.setStep(CurrentStep.COMPLETE);

        return state.getStep();
    }

    @UpdateValidatorMethod(updateName = "captureAdditionalDetails")
    public void captureAdditionalDetailsValidator(String additionalDetails) {
        if (state.getStep() != CurrentStep.WAITING_FOR_ADDITIONAL_DETAILS) {
            throw new IllegalArgumentException();
        }

        if (additionalDetails.isBlank()) {
            throw new IllegalArgumentException("All fields must be completed");
        }
    }


    //
    //  WORKFLOW ENTRY POINT
    //
    @Override
    public boolean onboard() {
        Workflow.await(() -> state.getStep() != CurrentStep.WAITING_FOR_BASIC_INFO);

        Workflow.await(() -> 
            state.getStep() != CurrentStep.WAITING_FOR_AADHAR_OTP && 
            state.getStep() != CurrentStep.VERIFYING_AADHAR_OTP &&
            state.getStep() != CurrentStep.RETRY_WAITING_FOR_AADHAR_OTP);

        Workflow.await(() -> state.getStep() != CurrentStep.WAITING_FOR_USER_DETAILS);

        Workflow.await(() -> state.getStep() != CurrentStep.WAITING_FOR_COMMUNICATION_DETAILS);

        // activity: send email otp

        Workflow.await(() -> 
            state.getStep() != CurrentStep.WAITING_FOR_EMAIL_OTP && 
            state.getStep() != CurrentStep.VERIFYING_EMAIL_OTP &&
            state.getStep() != CurrentStep.RETRY_WAITING_FOR_EMAIL_OTP);

        
        Workflow.await(() -> state.getStep() != CurrentStep.WAITING_FOR_ADDITIONAL_DETAILS);

        return true;
    }
}