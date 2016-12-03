package uk.gov.dwp.jms.manager.core.service.resources;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageId;
import uk.gov.dwp.jms.manager.client.FailedMessageReplayResource;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import java.util.List;
import java.util.function.Function;

public class FailedMessageReplayResourceImpl implements FailedMessageReplayResource {
    private final FailedMessageDao failedMessageDao;
    private final MessageSenderFactory messageSenderFactory;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;
    private final FailedMessageService failedMessageService;

    public FailedMessageReplayResourceImpl(FailedMessageDao failedMessageDao, MessageSenderFactory messageSenderFactory, Function<FailedMessage, MessageCreator> failedMessageCreatorFactory, FailedMessageService failedMessageService) {
        this.failedMessageDao = failedMessageDao;
        this.messageSenderFactory = messageSenderFactory;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
        this.failedMessageService = failedMessageService;
    }

    @Override
    public void replay(FailedMessageId messageId) {
        FailedMessage failedMessage = failedMessageDao.findById(messageId);
        messageSenderFactory.senderFor(failedMessage.getDestination().getBrokerName())
                .send(failedMessage.getDestination().getName(), failedMessageCreatorFactory.apply(failedMessage));
        failedMessageService.remove(failedMessage);
    }

    @Override
    public void replay(List<FailedMessageId> messageIds) {
        messageIds.forEach(this::replay);
    }
}
