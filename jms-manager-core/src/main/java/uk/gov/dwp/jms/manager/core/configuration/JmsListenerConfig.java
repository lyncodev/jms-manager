package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

@Configuration
public class JmsListenerConfig {

    @Bean
    @Autowired
    public DefaultMessageListenerContainer dlqMessageListenerContainer(ConnectionFactory connectionFactory, JmsListenerProperties jmsListenerProperties) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
        defaultMessageListenerContainer.setDestinationName(jmsListenerProperties.getDeadLetterQueueName());
        return defaultMessageListenerContainer;
    }
}
