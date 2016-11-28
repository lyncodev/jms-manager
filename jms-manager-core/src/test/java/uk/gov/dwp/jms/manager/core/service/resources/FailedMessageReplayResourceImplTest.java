package uk.gov.dwp.jms.manager.core.service.resources;

import org.junit.Test;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.FailedMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FailedMessageReplayResourceImplTest {
    private final FailedMessageCreatorFactory failedMessageCreatorFactory = mock(FailedMessageCreatorFactory.class);
    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private final FailedMessageRemoveService failedMessageRemoveService = mock(FailedMessageRemoveService.class);
    private FailedMessageReplayResourceImpl underTest = new FailedMessageReplayResourceImpl(
            failedMessageDao,
            messageSenderFactory,
            failedMessageCreatorFactory,
            failedMessageRemoveService
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
        verify(failedMessageRemoveService).remove(failedMessage);
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
        verify(failedMessageRemoveService).remove(failedMessage);
    }
}