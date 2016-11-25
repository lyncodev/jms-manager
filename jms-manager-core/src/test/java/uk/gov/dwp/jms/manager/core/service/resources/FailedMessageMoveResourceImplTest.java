package uk.gov.dwp.jms.manager.core.service.resources;

import org.junit.Test;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageMoveResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FailedMessageMoveResourceImplTest {
    private final FailedMessageRemoveService failedMessageRemoveService = mock(FailedMessageRemoveService.class);
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory = mock(Function.class);
    private final MessageSenderFactory messageSenderFactory = mock(MessageSenderFactory.class);
    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private FailedMessageMoveResourceImpl underTest = new FailedMessageMoveResourceImpl(
            failedMessageDao,
            messageSenderFactory,
            failedMessageCreatorFactory,
            failedMessageRemoveService
    );

    @Test
    public void moveSingle() throws Exception {
        String brokerName = "brokerName";
        String queueName = "queueName";
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();

        Destination destination = mock(Destination.class);
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageSender messageSender = mock(MessageSender.class);
        MessageCreator messageCreator = mock(MessageCreator.class);

        given(failedMessageDao.findById(failedMessageId)).willReturn(failedMessage);
        given(destination.getBrokerName()).willReturn(brokerName);
        given(destination.getName()).willReturn(queueName);
        given(messageSenderFactory.senderFor(brokerName)).willReturn(messageSender);
        given(failedMessageCreatorFactory.apply(failedMessage)).willReturn(messageCreator);

        underTest.move(failedMessageId, destination);

        verify(messageSender).send(queueName, messageCreator);
        verify(failedMessageRemoveService).remove(failedMessage);
    }

    @Test
    public void moveBulk() throws Exception {
        String brokerName = "brokerName";
        String queueName = "queueName";
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();

        Destination destination = mock(Destination.class);
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageSender messageSender = mock(MessageSender.class);
        MessageCreator messageCreator = mock(MessageCreator.class);

        given(failedMessageDao.findById(failedMessageId)).willReturn(failedMessage);
        given(destination.getBrokerName()).willReturn(brokerName);
        given(destination.getName()).willReturn(queueName);
        given(messageSenderFactory.senderFor(brokerName)).willReturn(messageSender);
        given(failedMessageCreatorFactory.apply(failedMessage)).willReturn(messageCreator);

        underTest.move(new FailedMessageMoveResource.Bulk(asList(failedMessageId), destination));

        verify(messageSender).send(queueName, messageCreator);
        verify(failedMessageRemoveService).remove(failedMessage);
    }
}