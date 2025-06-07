package com.lab.blps.components;

import com.lab.blps.services.MonetizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Delegate to fetch contract info: wraps MonetizationService.getContractInfo
 */
@Component("GetContractInfoDelegate")
public class GetContractInfoDelegate implements JavaDelegate {

    private final MonetizationService monetizationService;

    public GetContractInfoDelegate(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        Long contractId = (Long) execution.getVariable("contractId");
        String pdfPath = monetizationService.getContractInfo(contractId);
        execution.setVariable("pdfPath", pdfPath);
    }
}
