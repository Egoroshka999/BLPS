package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Delegate for updating an existing application.
 */
@Component("UpdateApplicationDelegate")
@Transactional
public class UpdateApplicationDelegate implements JavaDelegate {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public UpdateApplicationDelegate(ApplicationRepository applicationRepository,
                                     UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Long applicationId = (Long) execution.getVariable("applicationId");
        String name = (String) execution.getVariable("name");
        String description = (String) execution.getVariable("description");
        String appFilePath = (String) execution.getVariable("appFilePath");

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
            throw new RuntimeException("Access denied");
        }

        application.setName(name);
        application.setDescription(description);
        application.setAppFilePath(appFilePath);
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);
        execution.setVariable("applicationId", application.getId());
    }
}
