package com.lab.blps.components;

import com.lab.blps.jca.JiraConnection;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.models.applications.MonetizationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Delegate for uploading a new application and creating a JIRA issue.
 */
@Component("UploadApplicationDelegate")
@Transactional
public class UploadApplicationDelegate implements JavaDelegate {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final JiraConnection jira;

    public UploadApplicationDelegate(ApplicationRepository applicationRepository,
                                     UserService userService,
                                     JiraConnection jira) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        this.jira = jira;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String name = (String) execution.getVariable("name");
        String description = (String) execution.getVariable("description");
        String appFilePath = (String) execution.getVariable("appFilePath");

        Application application = new Application();
        application.setName(name);
        application.setDescription(description);
        application.setAppFilePath(appFilePath);
        application.setDeveloper(userService.getCurrentUser());
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setMonetizationStatus(MonetizationStatus.NONE);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);

        String key = jira.createIssue(
                "BLPS",
                "Новая заявка '" + name + "'",
                description
        );
        application.setExternalIssueKey(key);
        applicationRepository.save(application);

        execution.setVariable("applicationId", application.getId());
        execution.setVariable("externalIssueKey", key);
    }
}
