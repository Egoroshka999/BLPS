package com.lab.blps.config;

import com.lab.blps.jca.*;
import jakarta.resource.ResourceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jca.support.ResourceAdapterFactoryBean;

@Configuration
public class JcaConfig {
    @Value("${jira.url}")
    private String url;
    @Value("${jira.username}")
    private String username;
    @Value("${jira.password}")
    private String password;

    @Bean
    public ResourceAdapterFactoryBean resourceAdapter() {
        ResourceAdapterFactoryBean bean = new ResourceAdapterFactoryBean();
        bean.setResourceAdapter(new JiraResourceAdapter());
        return bean;
    }

    @Bean
    @Primary
    public JiraManagedConnectionFactory jiraManagedConnectionFactory() {
        JiraManagedConnectionFactory mcf = new JiraManagedConnectionFactory();
        mcf.setUrl(url);
        mcf.setUsername(username);
        mcf.setPassword(password);
        return mcf;
    }

    @Bean
    public JiraConnectionFactory jiraConnectionFactory(JiraManagedConnectionFactory mcf) {
        return new JiraConnectionFactoryImpl(mcf, null);
    }

    @Bean
    public JiraConnection jiraConnection(JiraConnectionFactory cf) throws ResourceException {
        return cf.getConnection();
    }
}