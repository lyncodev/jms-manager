package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.listener.MessageListenerContainer;
import uk.gov.dwp.jms.manager.core.jms.FailedMessageListenerFactory;
import uk.gov.dwp.jms.manager.core.jms.MessageListenerContainerFactory;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;
import uk.gov.dwp.jms.manager.core.jms.config.Brokers;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@Import({ResourceConfiguration.class})
public class JmsListenerConfig {
    @Autowired
    private Brokers brokers;

    @Autowired
    private JmsListenerProperties jmsListenerProperties;

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private FailedMessageService failedMessageService;

    @Autowired
    private FailedMessageClassifierProcessor failedMessageClassifierProcessor;

    @PostConstruct
    public void createListeners() throws IOException {

        String jmsListenerPropertiesQueueName = jmsListenerProperties.getQueueName();
        MessageListenerContainerFactory messageListenerContainerFactory = new MessageListenerContainerFactory(
                jmsListenerPropertiesQueueName, failedMessageService, failedMessageClassifierProcessor, new FailedMessageListenerFactory()
        );
        for (Broker connection : brokers.getBrokers()) {
            MessageListenerContainer messageListenerContainer = messageListenerContainerFactory.create(connection);
            beanFactory.registerSingleton(String.format("%sListener", connection), messageListenerContainer);
        }
    }

}
