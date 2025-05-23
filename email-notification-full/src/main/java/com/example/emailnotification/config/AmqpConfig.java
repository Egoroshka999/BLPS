package com.example.emailnotification.config;


import com.example.emailnotification.config.properties.RabbitProperties;
import jakarta.jms.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class AmqpConfig {
    private final RabbitProperties rabbitProperties;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setTransactionManager(new JmsTransactionManager(connectionFactory));
        factory.setSessionTransacted(true);
        factory.setConcurrency("1-1");
        return factory;
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        String brokerUrl = "amqp://rabbitmq:5672";
        JmsConnectionFactory qpidFactory = new JmsConnectionFactory(
                rabbitProperties.getUsername(),
                rabbitProperties.getPassword(),
                brokerUrl);
        CachingConnectionFactory cachingFactory = new CachingConnectionFactory(qpidFactory);
        cachingFactory.setSessionCacheSize(10);
        return cachingFactory;
    }
}
