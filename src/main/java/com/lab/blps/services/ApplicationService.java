package com.lab.blps.services;

import com.lab.blps.dtos.ApplicationDto;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.ApplicationStatus;
import com.lab.blps.models.applications.MonetizationStatus;
import com.lab.blps.models.applications.User;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.repositories.applications.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository,
                              UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    public Application uploadApplication(ApplicationDto applicationDto) {
        System.out.println("TRANSACTION ACTIVE: " + TransactionSynchronizationManager.isActualTransactionActive());

        Application application = new Application();
        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setAppFilePath(applicationDto.getAppFilePath());
        application.setDeveloper(userService.getCurrentUser());
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setMonetizationStatus(MonetizationStatus.NONE);

        applicationRepository.save(application);
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
        return applicationRepository.save(application);
    }

    public Page<Application> getAllByDeveloper(Pageable pageable) {
        return applicationRepository.findByDeveloperId(userService.getCurrentUser().getId(), pageable);
    }
}
