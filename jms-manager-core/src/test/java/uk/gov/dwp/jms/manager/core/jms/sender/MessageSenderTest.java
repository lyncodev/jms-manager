package uk.gov.dwp.jms.manager.core.jms.sender;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessageSenderTest {
    private JmsTemplate jmsTemplate = mock(JmsTemplate.class);
    private final MessageSender underTest = new MessageSender(jmsTemplate);

    @Test
    public void send() throws Exception {
        String queueName = "queue";
        MessageCreator messageCreator = mock(MessageCreator.class);

        underTest.send(queueName, messageCreator);

        verify(jmsTemplate).send(queueName, messageCreator);
    }
}