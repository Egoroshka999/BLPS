package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate for marking an application as deleted.
 */
@Component("DeleteApplicationDelegate")
@Transactional
public class DeleteApplicationDelegate implements JavaDelegate {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public DeleteApplicationDelegate(ApplicationRepository applicationRepository,
                                     UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Long applicationId = (Long) execution.getVariable("applicationId");

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
            throw new RuntimeException("Access denied");
        }

        application.setStatus(ApplicationStatus.DELETED);
        applicationRepository.save(application);
        execution.setVariable("applicationId", application.getId());
    }
}