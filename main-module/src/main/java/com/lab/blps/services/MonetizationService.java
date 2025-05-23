package com.lab.blps.services;

import com.lab.blps.controllers.EmailController;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.MonetizationStatus;
import com.lab.blps.models.applications.PaymentInfo;
import com.lab.blps.models.applications.PaymentStatus;
import com.lab.blps.models.contracts.Contract;
import com.lab.blps.models.contracts.ContractStatus;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.repositories.contracts.ContractRepository;
import com.lab.blps.repositories.applications.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MonetizationService {

    private final ApplicationRepository applicationRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final ContractRepository contractRepository;
    private final UserService userService;

    @Autowired
    private EmailController emailNotificationSender;


    public MonetizationService(ApplicationRepository applicationRepository,
                               PaymentInfoRepository paymentInfoRepository,
                               ContractRepository contractRepository, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.paymentInfoRepository = paymentInfoRepository;
        this.contractRepository = contractRepository;
        this.userService = userService;
    }

    /**
     * Developer подаёт заявку на монетизацию (при необходимости создаёт PaymentInfo).
     */
    @Transactional
    public Application requestMonetization(Long applicationId, String accountNumber) {
        Application application = checkDeveloperAccess(applicationId);

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
    public Contract createContractForApplication(Long applicationId, String pdfPath) throws Exception {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Предполагаем, что реквизиты уже проверены (paymentStatus = APPROVED).
        Contract contract = new Contract();
        contract.setApplicationId(applicationId);
        contract.setPdfPath(pdfPath);
        contract.setStatus(ContractStatus.SENT_TO_DEVELOPER);
        emailNotificationSender.sendMessage("Скачйте ваш контракт по ссылке" + pdfPath);

        contractRepository.save(contract);

        if (application.getName().equals("Dota2")) {
            throw new IllegalStateException("Dota2 govno");
        }

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
    public Contract handleContractResponse(Long contractId, boolean accepted) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        Long applicationId = contract.getApplicationId();
        Application application = applicationRepository.getApplicationById(applicationId);
        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
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
    public Application stopMonetization(Long applicationId) {
        Application application = checkDeveloperAccess(applicationId);
        application.setMonetizationStatus(MonetizationStatus.STOPPED);
        return applicationRepository.save(application);
    }

    private Application checkDeveloperAccess(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        if (!application.getDeveloper().equals(userService.getCurrentUser())) {
            throw new RuntimeException("Access denied");
        }
        return application;
    }
}

