package ru.sbrf.efs.rmkmcib.bht.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

/**
 * Created by sbt-manayev-iye on 02.08.2016.
 *
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"ru.sbrf.efs.rmkmcib.bht.app"})
public class RmKmAppConfig {

    @Value("${activemq.url}")
    String activeMqUrl;

    @Value("${activemq.queues.out}")
    String activeMqOutQueue;

    @Value("${activemq.queues.in}")
    String activeMqInQueue;

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        return jmsTemplate;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(activeMqUrl);
        return activeMQConnectionFactory;
    }


}
