package com.lab.blps.controllers;

import com.lab.blps.models.applications.Application;
import com.lab.blps.services.ModerationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moderator")
public class ModeratorController {

    private final ModerationService moderationService;

    public ModeratorController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/applications/{appId}/review")
    public Application reviewApplication(@PathVariable Long appId,
                                         @RequestParam boolean approved) {
        // Модератор проверяет приложение
        return moderationService.reviewApplication(appId, approved);
    }
}
