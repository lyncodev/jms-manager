package uk.gov.dwp.jms.manager.core.jms;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

public class MessageListenerContainerFactory {
    private final String queueName;
    private final FailedMessageResource failedMessageResource;
    private final FailedMessageListenerFactory messageListenerFactory;
    private final ConnectionFactoryFactory connectionFactoryFactory;

    public MessageListenerContainerFactory(String queueName, FailedMessageResource failedMessageResource, FailedMessageListenerFactory messageListenerFactory, ConnectionFactoryFactory connectionFactoryFactory) {
        this.queueName = queueName;
        this.failedMessageResource = failedMessageResource;
        this.messageListenerFactory = messageListenerFactory;
        this.connectionFactoryFactory = connectionFactoryFactory;
    }

    public MessageListenerContainer create (String brokerName) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactoryFactory.create(brokerName));
        defaultMessageListenerContainer.setDestinationName(queueName);
        defaultMessageListenerContainer.setupMessageListener(messageListenerFactory.create(failedMessageResource, brokerName));
        defaultMessageListenerContainer.afterPropertiesSet();
        return defaultMessageListenerContainer;
    }
}
