package com.brendan.temporal;

import java.util.stream.Stream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brendan.temporal.onboardingWorflow.CurrentStep;
import com.brendan.temporal.onboardingWorflow.OnboardingState;
import com.brendan.temporal.onboardingWorflow.OnboardingWorkflow;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowExecutionMetadata;
import io.temporal.client.WorkflowOptions;

@RestController
public class WorkflowController {

    @Autowired
    private WorkflowClient client;

    class Execution {
        public String workflowId, runId, status;

        public Execution(String workflowId, String runId, String status) {
            this.workflowId = workflowId;
            this.runId = runId;
            this.status = status;
        }
    }

    @GetMapping("/onboarding/list")
    @CrossOrigin(origins = "*")
    ArrayList<Execution> listOnboarding() {
        Stream<WorkflowExecutionMetadata> executionMetadataStream = client.listExecutions("WorkflowType=\"OnboardingWorkflow\"");

        ArrayList<Execution> executions = new ArrayList<>();

        executionMetadataStream.forEach(item -> {
            Execution ex = new Execution(
                item.getExecution().getWorkflowId(),
                item.getExecution().getRunId(),
                item.getStatus().toString()
            );

            executions.add(ex);
        });

        return executions;
    }

    @GetMapping("/onboarding/start")
    @CrossOrigin(origins = "*")
    Execution startOnboarding() throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class, 
            WorkflowOptions.newBuilder()
                .setTaskQueue("onboarding-queue")
                .build()
            );

        WorkflowExecution execution = WorkflowClient.start(workflow::onboard);

        Execution ex = new Execution(
            execution.getWorkflowId(),
            execution.getRunId(),
            ""
        );

        return ex;
    }

    @GetMapping("/onboarding/{id}/basic-info")
    @CrossOrigin(origins = "*")
    CurrentStep updateBasicInto(
        @PathVariable("id") String id, 
        @RequestParam(defaultValue = "") String aadhar, 
        @RequestParam(defaultValue = "") String pan,
        @RequestParam(defaultValue = "") String mobile)
    throws Exception {

        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureBasicInfo(aadhar, pan, mobile);
    }

    @GetMapping("/onboarding/{id}/aadhar-otp")
    @CrossOrigin(origins = "*")
    CurrentStep updateAadharOtp(@PathVariable("id") String id, @RequestParam(defaultValue = "") String aadharOtp) throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureAadharOTP(aadharOtp);
    }

    @GetMapping("/onboarding/{id}/user-details")
    @CrossOrigin(origins = "*")
    CurrentStep updateUserDetails(@PathVariable("id") String id, @RequestParam(defaultValue = "") String details) throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureUserDetails(details);
    }

    @GetMapping("/onboarding/{id}/communication-details")
    @CrossOrigin(origins = "*")
    CurrentStep updateCommunicationDetails(@PathVariable("id") String id, @RequestParam(defaultValue = "") String emailAddress) throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureCommunicationDetails(emailAddress);
    }

    @GetMapping("/onboarding/{id}/email-otp")
    @CrossOrigin(origins = "*")
    CurrentStep updateEmailOtp(@PathVariable("id") String id, @RequestParam(defaultValue = "") String emailOtp) throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureEmailOTP(emailOtp);
    }

    @GetMapping("/onboarding/{id}/additional-details")
    @CrossOrigin(origins = "*")
    CurrentStep updateAdditionalDetails(@PathVariable("id") String id, @RequestParam(defaultValue = "") String additionalDetails) throws Exception {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.captureAdditionalDetails(additionalDetails);
    }

    @GetMapping("/onboarding/{id}/current-state")
    @CrossOrigin(origins = "*")
    OnboardingState getCurrentState(@PathVariable("id") String id) {
        OnboardingWorkflow workflow = client.newWorkflowStub(
            OnboardingWorkflow.class,
            id
        );

        return workflow.getState();
    }
}
