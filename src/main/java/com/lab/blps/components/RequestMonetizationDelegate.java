package com.lab.blps.components;

import com.lab.blps.models.applications.Application;
import com.lab.blps.services.MonetizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate to request monetization: wraps MonetizationService.requestMonetization
 */
@Component("RequestMonetizationDelegate")
@Transactional
public class RequestMonetizationDelegate implements JavaDelegate {

    private final MonetizationService monetizationService;

    public RequestMonetizationDelegate(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long applicationId = (Long) execution.getVariable("applicationId");
        String accountNumber = (String) execution.getVariable("accountNumber");

        // Call service
        Application result = monetizationService.requestMonetization(applicationId, accountNumber);
        execution.setVariable("application", result);
    }
}
