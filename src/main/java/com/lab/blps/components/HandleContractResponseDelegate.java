package com.lab.blps.components;

import com.lab.blps.models.contracts.Contract;
import com.lab.blps.services.MonetizationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delegate to handle developer's contract response: wraps MonetizationService.handleContractResponse
 */
@Component("HandleContractResponseDelegate")
@Transactional
public class HandleContractResponseDelegate implements JavaDelegate {

    private final MonetizationService monetizationService;

    public HandleContractResponseDelegate(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long contractId = (Long) execution.getVariable("contractId");
        Boolean accepted = (Boolean) execution.getVariable("accepted");

        Contract contract = monetizationService.handleContractResponse(contractId, accepted);
        execution.setVariable("contract", contract);
    }
}
