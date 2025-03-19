package com.lab.blps.controllers;

import com.lab.blps.models.Application;
import com.lab.blps.services.ModerationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {

    private final ModerationService moderationService;

    public ModeratorController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PostMapping("/applications/{appId}/review")
    public Application reviewApplication(@PathVariable Long appId,
                                         @RequestParam boolean approved) {
        // Модератор проверяет приложение
        return moderationService.reviewApplication(appId, approved);
    }
}
