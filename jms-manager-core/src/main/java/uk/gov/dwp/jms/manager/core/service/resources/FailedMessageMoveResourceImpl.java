package uk.gov.dwp.jms.manager.core.service.resources;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageMoveResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

import java.util.function.Function;

public class FailedMessageMoveResourceImpl implements FailedMessageMoveResource {
    private final FailedMessageDao failedMessageDao;
    private final MessageSenderFactory messageSenderFactory;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;
    private final FailedMessageRemoveService failedMessageRemoveService;

    public FailedMessageMoveResourceImpl(FailedMessageDao failedMessageDao, MessageSenderFactory messageSenderFactory, Function<FailedMessage, MessageCreator> failedMessageCreatorFactory, FailedMessageRemoveService failedMessageRemoveService) {
        this.failedMessageDao = failedMessageDao;
        this.messageSenderFactory = messageSenderFactory;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
        this.failedMessageRemoveService = failedMessageRemoveService;
    }

    @Override
    public void move(FailedMessageId messageId, Destination destination) {
        FailedMessage failedMessage = failedMessageDao.findById(messageId);
        messageSenderFactory.senderFor(destination.getBrokerName())
                .send(destination.getName(), failedMessageCreatorFactory.apply(failedMessage));
        failedMessageRemoveService.remove(failedMessage);
    }

    @Override
    public void move(Bulk bulk) {
        bulk.getMessageIds().forEach(messageId -> move(messageId, bulk.getDestination()));
    }
}
