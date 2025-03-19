package com.lab.blps.services;

import com.lab.blps.models.Application;
import com.lab.blps.models.ApplicationStatus;
import com.lab.blps.repositories.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModerationService {

    private final ApplicationRepository applicationRepository;

    public ModerationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    public Application reviewApplication(Long applicationId, boolean approved) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (approved) {
            application.setStatus(ApplicationStatus.PUBLISHED);
        } else {
            application.setStatus(ApplicationStatus.REJECTED);
        }

        return applicationRepository.save(application);
    }
}

