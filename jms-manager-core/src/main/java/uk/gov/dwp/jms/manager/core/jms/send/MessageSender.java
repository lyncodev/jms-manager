package uk.gov.dwp.jms.manager.core.jms.send;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class MessageSender {
    private final JmsTemplate jmsTemplate;


    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(String queueName, MessageCreator messageCreator) {
        jmsTemplate.send(queueName, messageCreator);
    }
}
