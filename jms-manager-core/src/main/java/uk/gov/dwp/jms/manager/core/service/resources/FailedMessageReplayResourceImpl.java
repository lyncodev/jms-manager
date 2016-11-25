package uk.gov.dwp.jms.manager.core.service.resources;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageReplayResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

import java.util.List;
import java.util.function.Function;

public class FailedMessageReplayResourceImpl implements FailedMessageReplayResource {
    private final FailedMessageDao failedMessageDao;
    private final MessageSenderFactory messageSenderFactory;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;
    private final FailedMessageRemoveService failedMessageRemoveService;

    public FailedMessageReplayResourceImpl(FailedMessageDao failedMessageDao, MessageSenderFactory messageSenderFactory, Function<FailedMessage, MessageCreator> failedMessageCreatorFactory, FailedMessageRemoveService failedMessageRemoveService) {
        this.failedMessageDao = failedMessageDao;
        this.messageSenderFactory = messageSenderFactory;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
        this.failedMessageRemoveService = failedMessageRemoveService;
    }

    @Override
    public void replay(FailedMessageId messageId) {
        FailedMessage failedMessage = failedMessageDao.findById(messageId);
        messageSenderFactory.senderFor(failedMessage.getDestination().getBrokerName())
                .send(failedMessage.getDestination().getName(), failedMessageCreatorFactory.apply(failedMessage));
        failedMessageRemoveService.remove(failedMessage);
    }

    @Override
    public void replay(List<FailedMessageId> messageIds) {
        messageIds.forEach(this::replay);
    }
}
