package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.services.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Delegate for retrieving applications by the current developer.
 */
@Component("GetApplicationsByDeveloperDelegate")
public class GetApplicationsByDeveloperDelegate implements JavaDelegate {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;

    public GetApplicationsByDeveloperDelegate(ApplicationRepository applicationRepository,
                                              UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        int pageNumber = (int) execution.getVariable("pageNumber");
        int pageSize = (int) execution.getVariable("pageSize");
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Application> page = applicationRepository.findByDeveloperId(
                userService.getCurrentUser().getId(), pageable);

        List<Application> applications = page.getContent();
        execution.setVariable("applications", applications);
        execution.setVariable("totalPages", page.getTotalPages());
        execution.setVariable("totalElements", page.getTotalElements());
    }
}
