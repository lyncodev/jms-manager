package uk.gov.dwp.jms.manager.core.service.resources;

import org.junit.Test;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.SendMessageRequest;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;

import java.util.HashMap;
import java.util.function.Function;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class QueueResourceImplTest {
    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private Function<SendMessageRequest, MessageCreator> messageCreatorFunction = mock(Function.class);
    private QueueResourceImpl underTest = new QueueResourceImpl(messageSenderFactory, messageCreatorFunction);

    @Test
    public void sendMessage() throws Exception {
        MessageSender messageSender = mock(MessageSender.class);
        MessageCreator messageCreator = mock(MessageCreator.class);

        String brokerName = "brokerName";
        String queueName = "queueName";
        SendMessageRequest sendMessageRequest = new SendMessageRequest(
                new Destination(brokerName, queueName),
                "content",
                new HashMap<>()
        );

        given(messageSenderFactory.senderFor(brokerName)).willReturn(messageSender);
        given(messageCreatorFunction.apply(sendMessageRequest)).willReturn(messageCreator);

        underTest.sendMessage(sendMessageRequest);

        verify(messageSender).send(queueName, messageCreator);
    }
}