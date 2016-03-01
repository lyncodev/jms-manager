package uk.gov.dwp.jms.manager.core.jms.replay;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import java.util.function.Function;

import static org.mockito.Mockito.*;

public class FailedMessageReplayerTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();
    private static final String QUEUE_NAME = "uc.queue.name";

    private final JmsTemplate jmsTemplate = mock(JmsTemplate.class);
    private final FailedMessageService failedMessageService = mock(FailedMessageService.class);
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory = mock(Function.class);

    private final FailedMessageReplayer underTest = new FailedMessageReplayer(jmsTemplate, failedMessageService, failedMessageCreatorFactory);

    @Test
    public void testReplayMessage() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        when(failedMessage.getFailedMessageId()).thenReturn(FAILED_MESSAGE_ID);
        FailedMessageCreator failedMessageCreator = mock(FailedMessageCreator.class);

        when(failedMessageCreatorFactory.apply(failedMessage)).thenReturn(failedMessageCreator);

        underTest.replay(failedMessage, QUEUE_NAME);

        verify(jmsTemplate).send(QUEUE_NAME, failedMessageCreator);
        verify(failedMessageService).reprocess(FAILED_MESSAGE_ID);
    }
}