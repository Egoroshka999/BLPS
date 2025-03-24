package com.lab.blps.controllers;

import com.lab.blps.dtos.ApplicationDto;
import com.lab.blps.models.Application;
import com.lab.blps.services.ApplicationService;
import com.lab.blps.services.MonetizationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/applications")
    public List<Application> getMyApplications(@RequestParam Long developerId) {
        // На будущее developerId брать из токена аутентификации
        // и фильтровать приложения по developerId.
        // Здесь упрощённый вариант.
        return applicationService.getAllByDeveloper(developerId);
    }

    // Загрузить новое приложение
    @PostMapping("/applications")
    public Application uploadApplication(@RequestParam ApplicationDto applicationDto, @RequestParam Long developerId) {
        return applicationService.uploadApplication(applicationDto, developerId);
    }

    // Обновить приложение
    @PutMapping("/applications/{appId}")
    public Application updateApplication(@PathVariable Long appId, @PathVariable ApplicationDto applicationDto, @RequestParam Long developerId) {
        return applicationService.updateApplication(appId, applicationDto, developerId);
    }

    // Удалить приложение
    @DeleteMapping("/applications/{appId}")
    public Application deleteApplication(@PathVariable Long appId,
                                         @RequestParam Long developerId) {
        return applicationService.deleteApplication(appId, developerId);
    }

    // Подать заявку на монетизацию
    @PostMapping("/applications/{appId}/monetization/request")
    public Application requestMonetization(@PathVariable Long appId,
                                           @RequestParam Long developerId,
                                           @RequestParam String accountNumber) {
        return monetizationService.requestMonetization(appId, developerId, accountNumber);
    }

    // Остановить монетизацию
    @PostMapping("/applications/{appId}/monetization/stop")
    public Application stopMonetization(@PathVariable Long appId,
                                        @RequestParam Long developerId) {
        return monetizationService.stopMonetization(appId, developerId);
    }

    //Получение контракта на изучение
    @GetMapping("/contracts/{contractId}")
    public String getContract(@PathVariable Long contractId) {
        return monetizationService.getContractInfo(contractId);
    }
    // Ответ на договор
    @PostMapping("/contracts/{contractId}/response")
    public String contractResponse(@PathVariable Long contractId,
                                   @RequestParam Long developerId,
                                   @RequestParam boolean accepted) {
        monetizationService.handleContractResponse(contractId, developerId, accepted);
        return accepted ? "Developer accepted the contract" : "Developer rejected the contract";
    }
}
