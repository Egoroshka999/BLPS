package com.lab.blps.config;

import com.lab.blps.utils.jobs.MonetizationReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail monetizationReminderJobDetail() {
        return JobBuilder.newJob(MonetizationReminderJob.class)
                .withIdentity("monetizationReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger monetizationReminderTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .repeatHourlyForever(24)
                .withMisfireHandlingInstructionFireNow();

        return TriggerBuilder.newTrigger()
                .forJob(monetizationReminderJobDetail())
                .withIdentity("monetizationReminderTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
