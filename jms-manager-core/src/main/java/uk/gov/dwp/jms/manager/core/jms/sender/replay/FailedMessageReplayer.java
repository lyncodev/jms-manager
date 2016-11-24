package uk.gov.dwp.jms.manager.core.jms.sender.replay;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.jms.sender.MessageSenderFactory;

import java.util.function.Function;

public class FailedMessageReplayer {
    private final MessageSenderFactory messageSenderFactory;
    private final FailedMessageResource failedMessageResource;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;

    public FailedMessageReplayer(MessageSenderFactory messageSenderFactory, FailedMessageResource failedMessageResource,
                                 Function<FailedMessage, MessageCreator> failedMessageCreatorFactory) {
        this.messageSenderFactory = messageSenderFactory;
        this.failedMessageResource = failedMessageResource;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
    }

    public void replay(FailedMessage failedMessage) {
        messageSenderFactory.senderFor(failedMessage.getDestination().getBrokerName())
                .send(failedMessage.getDestination().getName(), failedMessageCreatorFactory.apply(failedMessage));
        failedMessageResource.reprocess(failedMessage.getFailedMessageId());
    }
}
