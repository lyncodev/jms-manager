package uk.gov.dwp.jms.manager.core.jms.sender.replay;

import org.junit.Test;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.jms.sender.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.sender.MessageSenderFactory;

import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FailedMessageReplayerTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();

    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private final FailedMessageResource failedMessageResource = mock(FailedMessageResource.class);
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory = mock(Function.class);

    private final FailedMessageReplayer underTest = new FailedMessageReplayer(
            messageSenderFactory, failedMessageResource,
            failedMessageCreatorFactory
    );

    @Test
    public void testReplayMessage() throws Exception {
        String brokerName = "broker";
        String queueName = "queue";

        FailedMessage failedMessage = mock(FailedMessage.class);
        FailedMessageCreator failedMessageCreator = mock(FailedMessageCreator.class);
        MessageSender messageSender = mock(MessageSender.class);

        when(failedMessage.getFailedMessageId()).thenReturn(FAILED_MESSAGE_ID);
        when(failedMessage.getDestination()).thenReturn(new Destination(brokerName, queueName));

        when(messageSenderFactory.senderFor(brokerName)).thenReturn(messageSender);
        when(failedMessageCreatorFactory.apply(failedMessage)).thenReturn(failedMessageCreator);

        underTest.replay(failedMessage);

        verify(messageSender).send(queueName, failedMessageCreator);
        verify(failedMessageResource).reprocess(FAILED_MESSAGE_ID);
    }
}