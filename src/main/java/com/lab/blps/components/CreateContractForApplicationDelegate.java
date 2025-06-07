package com.lab.blps.components;

import com.lab.blps.models.contracts.Contract;
import com.lab.blps.services.MonetizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate to create/send contract: wraps MonetizationService.createContractForApplication
 */
@Component("CreateContractForApplicationDelegate")
@Transactional
public class CreateContractForApplicationDelegate implements JavaDelegate {

    private final MonetizationService monetizationService;

    public CreateContractForApplicationDelegate(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long applicationId = (Long) execution.getVariable("applicationId");
        String pdfPath = (String) execution.getVariable("pdfPath");

        Contract contract = monetizationService.createContractForApplication(applicationId, pdfPath);
        execution.setVariable("contract", contract);
    }
}