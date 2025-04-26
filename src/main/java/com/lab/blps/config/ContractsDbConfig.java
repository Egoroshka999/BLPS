package com.lab.blps.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.lab.blps.repositories.contracts",
        entityManagerFactoryRef = "contractsEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class ContractsDbConfig {

    @Bean
    public DataSource contractsDataSource() throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:jboss/datasources/contracts_db");
        bean.afterPropertiesSet();
        System.out.println("LOOK HERE: DataSource contracts loaded: " + bean.getObject());
        return (DataSource) bean.getObject();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean contractsEntityManagerFactory(
            @Qualifier("contractsDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJtaDataSource(dataSource);
        em.setPackagesToScan("com.lab.blps.models.contracts");
        em.setPersistenceUnitName("ContractsPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = new HashMap<>();
//        props.put("hibernate.transaction.coordinator_class", "jta");
//        props.put("javax.persistence.transactionType", "JTA");
//        props.put("hibernate.transaction.jta.platform", "org.jboss.as.jpa.hibernate5.JBossAppServerJtaPlatform");
        props.put("hibernate.hbm2ddl.auto", "update");
        em.setJpaPropertyMap(props);

        return em;
    }
}
