package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.jca.JiraConnection;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate for reviewing (approving or rejecting) an application.
 */
@Component("ReviewApplicationDelegate")
@Transactional
public class ReviewApplicationDelegate implements JavaDelegate {

    private final ApplicationRepository applicationRepository;
    private final JiraConnection jira;

    public ReviewApplicationDelegate(ApplicationRepository applicationRepository,
                                     JiraConnection jira) {
        this.applicationRepository = applicationRepository;
        this.jira = jira;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long applicationId = (Long) execution.getVariable("applicationId");
        Boolean approved = (Boolean) execution.getVariable("approved");

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (Boolean.TRUE.equals(approved)) {
            application.setStatus(ApplicationStatus.PUBLISHED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        applicationRepository.save(application);

        jira.transitionIssue(
                application.getExternalIssueKey(),
                approved ? "Approve" : "Reject"
        );

        execution.setVariable("application", application);
    }
}
