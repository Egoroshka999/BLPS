package com.lab.blps.services;

import com.lab.blps.jca.JiraConnection;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModerationService {

    private final ApplicationRepository applicationRepository;

    private final JiraConnection jira;

    public ModerationService(ApplicationRepository applicationRepository, JiraConnection jira) {
        this.applicationRepository = applicationRepository;
        this.jira = jira;
    }

    @Transactional
    public Application reviewApplication(Long applicationId, boolean approved) throws Exception {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (approved) {
            application.setStatus(ApplicationStatus.PUBLISHED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        applicationRepository.save(application);

        jira.transitionIssue(
                application.getExternalIssueKey(),
                approved ? "Approve" : "Reject"
        );

        return application;
    }
}

