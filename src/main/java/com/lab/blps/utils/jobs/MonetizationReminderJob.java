package com.lab.blps.utils.jobs;

import com.lab.blps.controllers.EmailController;
import com.lab.blps.models.applications.Application;
import com.lab.blps.models.applications.User;
import com.lab.blps.repositories.applications.ApplicationRepository;
import com.lab.blps.repositories.applications.UserRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MonetizationReminderJob implements Job {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private EmailController emailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext context) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Application> apps = applicationRepository.findByMonetizationNotRequestedAndCreatedAtBefore(oneWeekAgo);

        for (Application app : apps) {
            User dev = app.getDeveloper();
            if (dev.getEmail() != null) {
                emailSender.sendMessage("Вы ещё не подали заявку на монетизацию приложения: " + app.getName());
            }
        }
    }
}
