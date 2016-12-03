package uk.gov.dwp.jms.manager.core.service.resources;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.quartz.Scheduler;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;

import java.util.HashMap;
import java.util.function.Function;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SendMessageResourceImplTest {
    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private final Function<SendMessageRequest, MessageCreator> messageCreatorFunction = mock(Function.class);
    private final Scheduler scheduler = mock(Scheduler.class);
    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private SendMessageResourceImpl underTest = new SendMessageResourceImpl(messageSenderFactory, messageCreatorFunction, scheduler, objectMapper);

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