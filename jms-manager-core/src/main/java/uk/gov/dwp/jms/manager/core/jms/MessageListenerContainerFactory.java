package uk.gov.dwp.jms.manager.core.jms;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

public class MessageListenerContainerFactory {
    private final String queueName;
    private final FailedMessageService failedMessageService;
    private final FailedMessageClassifierProcessor failedMessageClassifierProcessor;
    private final FailedMessageListenerFactory messageListenerFactory;

    public MessageListenerContainerFactory(String queueName, FailedMessageService failedMessageService, FailedMessageClassifierProcessor failedMessageClassifierProcessor, FailedMessageListenerFactory messageListenerFactory) {
        this.queueName = queueName;
        this.failedMessageService = failedMessageService;
        this.failedMessageClassifierProcessor = failedMessageClassifierProcessor;
        this.messageListenerFactory = messageListenerFactory;
    }

    public MessageListenerContainer create (Broker broker) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(broker.createConnectionFactory());
        defaultMessageListenerContainer.setDestinationName(queueName);
        defaultMessageListenerContainer.setupMessageListener(messageListenerFactory.create(failedMessageService, failedMessageClassifierProcessor, broker.getName()));
        defaultMessageListenerContainer.afterPropertiesSet();
        return defaultMessageListenerContainer;
    }
}
