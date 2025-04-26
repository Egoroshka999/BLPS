package com.lab.blps.controllers;

import com.lab.blps.models.contracts.Contract;
import com.lab.blps.models.applications.PaymentInfo;
import com.lab.blps.models.applications.PaymentStatus;
import com.lab.blps.services.MonetizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final MonetizationService monetizationService;

    public FinanceController(MonetizationService monetizationService) {
        this.monetizationService = monetizationService;
    }

    // Проверить корректность реквизитов
    @PreAuthorize("hasRole('FINANCIER')")
    @PostMapping("/paymentinfo/{paymentInfoId}/validate")
    public PaymentInfo validatePaymentInfo(@PathVariable Long paymentInfoId,
                                           @RequestParam PaymentStatus paymentStatus) {
        return monetizationService.validatePaymentInfo(paymentInfoId, paymentStatus);
    }

    // Создать договор (после проверки реквизитов)
    @PreAuthorize("hasRole('FINANCIER')")
    @PostMapping("/contracts")
    public Contract createContract(@RequestParam Long applicationId,
                                   @RequestParam String pdfPath) throws Exception {
        return monetizationService.createContractForApplication(applicationId, pdfPath);
    }
}

