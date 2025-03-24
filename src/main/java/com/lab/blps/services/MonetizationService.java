package com.lab.blps.services;

import com.lab.blps.models.*;
import com.lab.blps.repositories.ApplicationRepository;
import com.lab.blps.repositories.ContractRepository;
import com.lab.blps.repositories.PaymentInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MonetizationService {

    private final ApplicationRepository applicationRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final ContractRepository contractRepository;

    public MonetizationService(ApplicationRepository applicationRepository,
                               PaymentInfoRepository paymentInfoRepository,
                               ContractRepository contractRepository) {
        this.applicationRepository = applicationRepository;
        this.paymentInfoRepository = paymentInfoRepository;
        this.contractRepository = contractRepository;
    }

    /**
     * Developer подаёт заявку на монетизацию (при необходимости создаёт PaymentInfo).
     */
    @Transactional
    public Application requestMonetization(Long applicationId, Long developerId, String accountNumber) {
        Application application = checkDeveloperAccess(applicationId, developerId);

        // Создать/обновить платежные реквизиты

        /*
         Я вот думаю над тем, что если уже есть у нас PaymentInfo для девелопера,
         тогда нам не надо создавать, так что добавил проверку
         */
        if(paymentInfoRepository.getPaymentInfoByAccountNumber(accountNumber) != null) {
            var paymentInfo = paymentInfoRepository.getPaymentInfoByAccountNumber(accountNumber);
            paymentInfo.setPaymentStatus(PaymentStatus.APPROVED);
        }
        else {
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setAccountNumber(accountNumber);
            paymentInfo.setDeveloper(application.getDeveloper());
            paymentInfo.setPaymentStatus(PaymentStatus.NONE); // пока не проверено Finance Dept
            paymentInfoRepository.save(paymentInfo);
        }

        application.setMonetizationStatus(MonetizationStatus.REQUESTED);
        applicationRepository.save(application);

        return application;
    }

    /**
     * Finance проверяет реквизиты (valid / invalid).
     */
    @Transactional
    public PaymentInfo validatePaymentInfo(Long paymentInfoId, PaymentStatus paymentStatus) {
        PaymentInfo paymentInfo = paymentInfoRepository.findById(paymentInfoId)
                .orElseThrow(() -> new RuntimeException("Payment info not found"));

        paymentInfo.setPaymentStatus(paymentStatus);
        return paymentInfoRepository.save(paymentInfo);
    }

    /**
     * Если реквизиты корректны, создаём договор и выставляем приложение в статус PENDING_CONTRACT.
     */
    @Transactional
    public Contract createContractForApplication(Long applicationId, String pdfPath) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Предполагаем, что реквизиты уже проверены (paymentStatus = APPROVED).
        Contract contract = new Contract();
        contract.setApplication(application);
        contract.setPdfPath(pdfPath);
        contract.setStatus(ContractStatus.SENT_TO_DEVELOPER);

        contractRepository.save(contract);

        application.setMonetizationStatus(MonetizationStatus.PENDING_CONTRACT);
        applicationRepository.save(application);

        return contract;
    }

    /**
     * Developer Получает детали контракта для ознакомления
     */
    public String getContractInfo(Long contrartId){
        Contract contract = contractRepository.findById(contrartId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        return contract.getPdfPath();
    }
    /**
     * Developer соглашается/отказывается от договора
     */
    @Transactional
    public Contract handleContractResponse(Long contractId, Long developerId, boolean accepted) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        Application application = contract.getApplication();
        if (!application.getDeveloper().getId().equals(developerId)) {
            throw new RuntimeException("Access denied");
        }

        if (accepted) {
            contract.setStatus(ContractStatus.APPROVED_BY_DEVELOPER);
            application.setMonetizationStatus(MonetizationStatus.ACTIVE);
        } else {
            contract.setStatus(ContractStatus.REJECTED_BY_DEVELOPER);
            // Монетизация не будет подключена
            application.setMonetizationStatus(MonetizationStatus.NONE);
        }

        contractRepository.save(contract);
        applicationRepository.save(application);

        return contract;
    }

    /**
     * Developer останавливает монетизацию
     */
    @Transactional
    public Application stopMonetization(Long applicationId, Long developerId) {
        Application application = checkDeveloperAccess(applicationId, developerId);
        application.setMonetizationStatus(MonetizationStatus.STOPPED);
        return applicationRepository.save(application);
    }

    private Application checkDeveloperAccess(Long applicationId, Long developerId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        if (!application.getDeveloper().getId().equals(developerId)) {
            throw new RuntimeException("Access denied");
        }
        return application;
    }
}

