package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.services.MonetizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate to stop monetization: wraps MonetizationService.stopMonetization
 */
@Component("StopMonetizationDelegate")
@Transactional
public class StopMonetizationDelegate implements JavaDelegate {

    private final MonetizationService monetizationService;

    public StopMonetizationDelegate(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long applicationId = (Long) execution.getVariable("applicationId");

        Application application = monetizationService.stopMonetization(applicationId);
        execution.setVariable("application", application);
    }
}
