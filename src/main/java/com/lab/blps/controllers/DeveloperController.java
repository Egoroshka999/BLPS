package com.lab.blps.controllers;

import com.lab.blps.dtos.ApplicationDto;
import com.lab.blps.models.applications.Application;
import com.lab.blps.services.ApplicationService;
import com.lab.blps.services.MonetizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/developer")
public class DeveloperController {

    private final ApplicationService applicationService;
    private final MonetizationService monetizationService;

    public DeveloperController(ApplicationService applicationService,
                               MonetizationService monetizationService) {
        this.applicationService = applicationService;
        this.monetizationService = monetizationService;
    }

    // Получить список приложений текущего разработчика
    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/applications")
    public Page<Application> getMyApplications(Pageable pageable) {
        // На будущее developerId брать из токена аутентификации
        // и фильтровать приложения по developerId.
        // Здесь упрощённый вариант.
        return applicationService.getAllByDeveloper(pageable);
    }

    // Загрузить новое приложение
    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/applications")
    public Application uploadApplication(@RequestBody ApplicationDto applicationDto) throws Exception {
        return applicationService.uploadApplication(applicationDto);
    }

    // Обновить приложение
    @PreAuthorize("hasRole('DEVELOPER')")
    @PutMapping("/applications/{appId}")
    public Application updateApplication(@PathVariable Long appId, @RequestBody ApplicationDto applicationDto) {
        return applicationService.updateApplication(appId, applicationDto);
    }

    // Удалить приложение
    @PreAuthorize("hasRole('DEVELOPER')")
    @DeleteMapping("/applications/{appId}")
    public Application deleteApplication(@PathVariable Long appId) {
        return applicationService.deleteApplication(appId);
    }

    // Подать заявку на монетизацию
    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/applications/{appId}/monetization/request")
    public Application requestMonetization(@PathVariable Long appId,
                                           @RequestParam String accountNumber) {
        return monetizationService.requestMonetization(appId, accountNumber);
    }

    // Остановить монетизацию
    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/applications/{appId}/monetization/stop")
    public Application stopMonetization(@PathVariable Long appId) {
        return monetizationService.stopMonetization(appId);
    }

    //Получение контракта на изучение
    @PreAuthorize("hasRole('DEVELOPER')")
    @GetMapping("/contracts/{contractId}")
    public String getContract(@PathVariable Long contractId) {
        return monetizationService.getContractInfo(contractId);
    }
    // Ответ на договор
    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping("/contracts/{contractId}/response")
    public String contractResponse(@PathVariable Long contractId,
                                   @RequestParam boolean accepted) {
        monetizationService.handleContractResponse(contractId, accepted);
        return accepted ? "Developer accepted the contract" : "Developer rejected the contract";
    }
}
