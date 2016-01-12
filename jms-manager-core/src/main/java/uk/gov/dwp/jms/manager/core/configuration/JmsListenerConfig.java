package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import uk.gov.dwp.jms.manager.core.jms.FailedMessageListener;
import uk.gov.dwp.jms.manager.core.jms.ActiveMQMessagePropertyExtractor;
import uk.gov.dwp.jms.manager.core.jms.MessageTextExtractor;
import uk.gov.dwp.jms.manager.core.jms.MessageWithPropertiesAdapterListener;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import javax.jms.ConnectionFactory;

@Configuration
@Import({ServiceConfig.class})
public class JmsListenerConfig {

    @Bean
    @Autowired
    public MessageWithPropertiesAdapterListener messageWithPropertiesAdapterListener(FailedMessageListener failedMessageListener) {
        return new MessageWithPropertiesAdapterListener(new MessageTextExtractor(), new ActiveMQMessagePropertyExtractor(), failedMessageListener);
    }

    @Bean
    public FailedMessageListener failedMessageListener(FailedMessageService failedMessageService) {
        return new FailedMessageListener(failedMessageService);
    }

    @Bean
    @Autowired
    public DefaultMessageListenerContainer dlqMessageListenerContainer(ConnectionFactory connectionFactory, JmsListenerProperties jmsListenerProperties, MessageWithPropertiesAdapterListener messageWithPropertiesAdapterListener) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
        defaultMessageListenerContainer.setDestinationName(jmsListenerProperties.getQueueName());
        defaultMessageListenerContainer.setupMessageListener(messageWithPropertiesAdapterListener);
        return defaultMessageListenerContainer;
    }
}
