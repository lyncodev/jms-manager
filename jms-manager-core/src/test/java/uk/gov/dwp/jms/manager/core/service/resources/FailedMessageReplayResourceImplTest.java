package uk.gov.dwp.jms.manager.core.service.resources;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageId;
import org.junit.Test;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.FailedMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FailedMessageReplayResourceImplTest {
    private final FailedMessageCreatorFactory failedMessageCreatorFactory = mock(FailedMessageCreatorFactory.class);
    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private final FailedMessageService failedMessageService = mock(FailedMessageService.class);
    private FailedMessageReplayResourceImpl underTest = new FailedMessageReplayResourceImpl(
            failedMessageDao,
            messageSenderFactory,
            failedMessageCreatorFactory,
            failedMessageService
    );

    @Test
    public void replay() throws Exception {
        String brokerName = "brokerName";
        String queueName = "queueName";

        FailedMessage failedMessage = mock(FailedMessage.class);
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
        Destination destination = mock(Destination.class);
        MessageSender messageSender = mock(MessageSender.class);
        MessageCreator messageCreator = mock(MessageCreator.class);

        given(failedMessageDao.findById(failedMessageId)).willReturn(failedMessage);
        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(brokerName);
        given(destination.getName()).willReturn(queueName);
        given(messageSenderFactory.senderFor(brokerName)).willReturn(messageSender);
        given(failedMessageCreatorFactory.apply(failedMessage)).willReturn(messageCreator);

        underTest.replay(failedMessageId);

        verify(messageSender).send(queueName, messageCreator);
        verify(failedMessageService).remove(failedMessage);
    }

    @Test
    public void replayMultiple() throws Exception {
        String brokerName = "brokerName";
        String queueName = "queueName";

        FailedMessage failedMessage = mock(FailedMessage.class);
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
        Destination destination = mock(Destination.class);
        MessageSender messageSender = mock(MessageSender.class);
        MessageCreator messageCreator = mock(MessageCreator.class);

        given(failedMessageDao.findById(failedMessageId)).willReturn(failedMessage);
        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(brokerName);
        given(destination.getName()).willReturn(queueName);
        given(messageSenderFactory.senderFor(brokerName)).willReturn(messageSender);
        given(failedMessageCreatorFactory.apply(failedMessage)).willReturn(messageCreator);

        underTest.replay(asList(failedMessageId));

        verify(messageSender).send(queueName, messageCreator);
        verify(failedMessageService).remove(failedMessage);
    }
}