package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jms.listener.MessageListenerContainer;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.jms.ConnectionFactoryFactory;
import uk.gov.dwp.jms.manager.core.jms.FailedMessageListenerFactory;
import uk.gov.dwp.jms.manager.core.jms.MessageListenerContainerFactory;

import javax.annotation.PostConstruct;

@Configuration
@Import({ResourceConfiguration.class})
public class JmsListenerConfig {
    @Autowired
    private Environment environment;

    @Autowired
    private JmsListenerProperties jmsListenerProperties;

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Autowired
    private FailedMessageResource failedMessageResource;

    @PostConstruct
    public void createListeners() {
        MessageListenerContainerFactory messageListenerContainerFactory = messageListenerContainerFactory(jmsListenerProperties, failedMessageResource);
        String[] brokers = environment.getProperty("brokers", String[].class, new String[]{"default"});
        for (String broker : brokers) {
            MessageListenerContainer messageListenerContainer = messageListenerContainerFactory.create(broker);
            beanFactory.registerSingleton(String.format("%sListener", broker), messageListenerContainer);
        }
    }

    private MessageListenerContainerFactory messageListenerContainerFactory(JmsListenerProperties jmsListenerProperties, FailedMessageResource failedMessageResource) {
        String jmsListenerPropertiesQueueName = jmsListenerProperties.getQueueName();
        return new MessageListenerContainerFactory(
                jmsListenerPropertiesQueueName, failedMessageResource, new FailedMessageListenerFactory(), new ConnectionFactoryFactory(environment)
        );
    }
}
