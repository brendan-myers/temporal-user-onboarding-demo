package com.brendan.temporal.onboardingWorflow;

import java.time.Duration;

import org.springframework.stereotype.Component;

import io.temporal.spring.boot.ActivityImpl;

@Component
@ActivityImpl(taskQueues = "onboarding-queue")
public class OnboardingActivityImpl implements OnboardingActivity {

    @Override
    public boolean verifyAadharOtp(String aadharOtp) {
        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (Exception e) {
            return false;
        }
        
        return aadharOtp.equals("1234");
    }

    @Override
    public boolean verifyEmailOtp(String emailOtp) {
        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (Exception e) {
            return false;
        }
        
        return emailOtp.equals("1234");
    }
    
}
