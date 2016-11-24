package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.core.env.Environment;

import javax.jms.ConnectionFactory;

public class ConnectionFactoryFactory {
    private final Environment environment;

    public ConnectionFactoryFactory(Environment environment) {
        this.environment = environment;
    }

    public ConnectionFactory create (String brokerName) {
        String brokerURL = environment.getProperty(String.format("broker.%s.url", brokerName));
        String brokerPassword = environment.getProperty(String.format("broker.%s.password", brokerName));
        String brokerUsername = environment.getProperty(String.format("broker.%s.username", brokerName));
        return new ActiveMQConnectionFactory(brokerUsername, brokerPassword, brokerURL);
    }
}
