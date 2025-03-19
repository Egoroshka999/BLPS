package com.lab.blps.services;

import com.lab.blps.models.Application;
import com.lab.blps.models.ApplicationStatus;
import com.lab.blps.models.MonetizationStatus;
import com.lab.blps.models.User;
import com.lab.blps.repositories.ApplicationRepository;
import com.lab.blps.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Application uploadApplication(Long developerId, String name, String description, String filePath) {
        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        Application application = new Application();
        application.setName(name);
        application.setDescription(description);
        application.setAppFilePath(filePath);
        application.setDeveloper(developer);
        application.setStatus(ApplicationStatus.UPLOADED);
        application.setMonetizationStatus(MonetizationStatus.NONE);

        return applicationRepository.save(application);
    }

    @Transactional
    public Application deleteApplication(Long applicationId, Long developerId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().getId().equals(developerId)) {
            throw new RuntimeException("Access denied");
        }
        application.setStatus(ApplicationStatus.DELETED);
        return applicationRepository.save(application);
    }

    @Transactional
    public Application updateApplication(Long applicationId, Long developerId,
                                         String newName, String newDescription) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().getId().equals(developerId)) {
            throw new RuntimeException("Access denied");
        }

        application.setName(newName);
        application.setDescription(newDescription);
        return applicationRepository.save(application);
    }

    public List<Application> getAllByDeveloper(Long developerId) {
        return applicationRepository.findAll().stream()
                .filter(app -> app.getDeveloper().getId().equals(developerId))
                .collect(Collectors.toList());
    }


    // и т.д. методы для получения списка приложений, подробной инфы и т.п.
}
