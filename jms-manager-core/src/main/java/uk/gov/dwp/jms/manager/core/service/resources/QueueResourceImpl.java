package uk.gov.dwp.jms.manager.core.service.resources;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.QueueResource;
import uk.gov.dwp.jms.manager.core.client.SendMessageRequest;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;

import java.util.function.Function;

public class QueueResourceImpl implements QueueResource {
    private final MessageSenderFactory messageSenderFactory;
    private final Function<SendMessageRequest, MessageCreator> messageCreatorFunction;

    public QueueResourceImpl(MessageSenderFactory messageSenderFactory, Function<SendMessageRequest, MessageCreator> messageCreatorFunction) {
        this.messageSenderFactory = messageSenderFactory;
        this.messageCreatorFunction = messageCreatorFunction;
    }

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest) {
        messageSenderFactory
                .senderFor(sendMessageRequest.getDestination().getBrokerName())
                .send(sendMessageRequest.getDestination().getName(), messageCreatorFunction.apply(sendMessageRequest));
    }
}
