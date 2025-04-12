package com.lab.blps.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = "com.lab.blps.repositories.applications",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class PrimaryDbConfig {

    @Bean
    @Primary
    public DataSource primaryDataSource() throws NamingException {
        // Пример JNDI
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setJndiName("java:jboss/datasources/google_play_db");
        bean.afterPropertiesSet();
        System.out.println("LOOK HERE: DataSource primary loaded: " + bean.getObject());
        return (DataSource) bean.getObject();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier("primaryDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setJtaDataSource(dataSource);
        em.setPackagesToScan("com.lab.blps.models.applications"); // где лежат @Entity
        em.setPersistenceUnitName("PrimaryPU");

        // Настройка Hibernate/Provider
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> props = new HashMap<>();
        // Указываем JTA-платформу WildFly, чтобы Hibernate знал про контейнерную транзакцию
//        props.put("hibernate.transaction.jta.platform", "org.jboss.as.jpa.hibernate5.JBossAppServerJtaPlatform");
        props.put("hibernate.hbm2ddl.auto", "update");
        em.setJpaPropertyMap(props);

        return em;
    }
}

