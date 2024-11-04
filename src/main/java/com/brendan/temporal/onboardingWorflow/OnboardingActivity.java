package com.brendan.temporal.onboardingWorflow;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface OnboardingActivity {
    boolean verifyAadharOtp(String aadharOtp);
    boolean verifyEmailOtp(String emailOtp);
}
