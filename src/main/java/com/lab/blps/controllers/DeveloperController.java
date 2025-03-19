package com.lab.blps.controllers;

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
    public Application uploadApplication(@RequestParam Long developerId,
                                         @RequestParam String name,
                                         @RequestParam String description,
                                         @RequestParam String filePath) {
        return applicationService.uploadApplication(developerId, name, description, filePath);
    }

    // Обновить приложение
    @PutMapping("/applications/{appId}")
    public Application updateApplication(@PathVariable Long appId,
                                         @RequestParam Long developerId,
                                         @RequestParam String newName,
                                         @RequestParam String newDescription) {
        return applicationService.updateApplication(appId, developerId, newName, newDescription);
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

    // Ответ на договор
    @PostMapping("/contracts/{contractId}/response")
    public String contractResponse(@PathVariable Long contractId,
                                   @RequestParam Long developerId,
                                   @RequestParam boolean accepted) {
        monetizationService.handleContractResponse(contractId, developerId, accepted);
        return accepted ? "Developer accepted the contract" : "Developer rejected the contract";
    }
}
