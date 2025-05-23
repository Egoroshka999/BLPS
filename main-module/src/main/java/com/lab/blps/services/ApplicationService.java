package com.lab.blps.services;

import com.lab.blps.dtos.ApplicationDto;
import com.lab.blps.jca.JiraConnection;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.models.applications.MonetizationStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.repositories.applications.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    private final JiraConnection jira;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository,
                              UserService userService, JiraConnection jira) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jira = jira;
    }

    @Transactional
    public Application uploadApplication(ApplicationDto applicationDto) throws Exception {
        System.out.println("TRANSACTION ACTIVE: " + TransactionSynchronizationManager.isActualTransactionActive());

        Application application = new Application();
        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setAppFilePath(applicationDto.getAppFilePath());
        application.setDeveloper(userService.getCurrentUser());
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setMonetizationStatus(MonetizationStatus.NONE);
        application.setCreatedAt(LocalDateTime.now());

        applicationRepository.save(application);

        String key = jira.createIssue(
                "BLPS",
                "Новая заявка '" + application.getName() + "'",
                application.getDescription()
        );
        application.setExternalIssueKey(key);

        return application;
    }

    @Transactional
    public Application deleteApplication(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
            throw new RuntimeException("Access denied");
        }
        application.setStatus(ApplicationStatus.DELETED);
        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateApplication(Long applicationId, ApplicationDto applicationDto) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
            throw new RuntimeException("Access denied");
        }

        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setAppFilePath(applicationDto.getAppFilePath());
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setCreatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    public Page<Application> getAllByDeveloper(Pageable pageable) {
        return applicationRepository.findByDeveloperId(userService.getCurrentUser().getId(), pageable);
    }
}
