package uk.gov.dwp.jms.manager.core.service.resources;

import client.Destination;
import client.FailedMessage;
import client.FailedMessageId;
import client.FailedMessageMoveResource;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import java.util.function.Function;

public class FailedMessageMoveResourceImpl implements FailedMessageMoveResource {
    private final FailedMessageDao failedMessageDao;
    private final MessageSenderFactory messageSenderFactory;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;
    private final FailedMessageService failedMessageService;

    public FailedMessageMoveResourceImpl(FailedMessageDao failedMessageDao, MessageSenderFactory messageSenderFactory, Function<FailedMessage, MessageCreator> failedMessageCreatorFactory, FailedMessageService failedMessageService) {
        this.failedMessageDao = failedMessageDao;
        this.messageSenderFactory = messageSenderFactory;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
        this.failedMessageService = failedMessageService;
    }

    @Override
    public void move(FailedMessageId messageId, Destination destination) {
        FailedMessage failedMessage = failedMessageDao.findById(messageId);
        messageSenderFactory.senderFor(destination.getBrokerName())
                .send(destination.getName(), failedMessageCreatorFactory.apply(failedMessage));
        failedMessageService.remove(failedMessage);
    }

    @Override
    public void move(Bulk bulk) {
        bulk.getMessageIds().forEach(messageId -> move(messageId, bulk.getDestination()));
    }
}
