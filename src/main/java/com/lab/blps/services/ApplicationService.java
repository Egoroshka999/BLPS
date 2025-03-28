package com.lab.blps.services;

import com.lab.blps.dtos.ApplicationDto;
import com.lab.blps.models.Application;
import com.lab.blps.models.ApplicationStatus;
import com.lab.blps.models.MonetizationStatus;
import com.lab.blps.models.User;
import com.lab.blps.repositories.ApplicationRepository;
import com.lab.blps.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Application uploadApplication(ApplicationDto applicationDto, Long developerId) {
        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new RuntimeException("Developer not found"));

        Application application = new Application();
        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setAppFilePath(applicationDto.getAppFilePath());
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
    public Application updateApplication(Long applicationId, ApplicationDto applicationDto, Long developerId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getDeveloper().getId().equals(developerId)) {
            throw new RuntimeException("Access denied");
        }

        application.setName(applicationDto.getName());
        application.setDescription(applicationDto.getDescription());
        application.setAppFilePath(applicationDto.getAppFilePath());
        application.setStatus(ApplicationStatus.UPLOADED);
        return applicationRepository.save(application);
    }

    public Page<Application> getAllByDeveloper(Long developerId, Pageable pageable) {
        return applicationRepository.findByDeveloperId(developerId, pageable);
    }


    // и т.д. методы для получения списка приложений, подробной инфы и т.п.
    /*
      Здесь вроде все есть хз что еще добавить, не совсем понял про подробную инфу
     */
}
