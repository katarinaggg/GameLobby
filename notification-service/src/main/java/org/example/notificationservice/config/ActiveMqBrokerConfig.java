package org.example.notificationservice.config;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActiveMqBrokerConfig {

    @Bean
    public BrokerService broker() throws Exception {
        BrokerService broker = new BrokerService();

        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector("tcp://localhost:61616");

        return broker;
    }
}
