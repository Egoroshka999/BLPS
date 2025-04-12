package com.lab.blps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class JtaConfig {

    @Bean(name = "transactionManager")
    public JtaTransactionManager transactionManager() {
        return new JtaTransactionManager();
    }
}
