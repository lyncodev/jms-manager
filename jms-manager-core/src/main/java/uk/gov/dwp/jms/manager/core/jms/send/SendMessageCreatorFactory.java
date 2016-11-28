package uk.gov.dwp.jms.manager.core.jms.send;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.SendMessageRequest;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.MessageDecorator;

import java.util.Collection;
import java.util.function.Function;

public class SendMessageCreatorFactory implements Function<SendMessageRequest, MessageCreator> {
    private final Collection<MessageDecorator> messageDecorators;

    public SendMessageCreatorFactory(Collection<MessageDecorator> messageDecorators) {
        this.messageDecorators = messageDecorators;
    }

    @Override
    public MessageCreator apply(SendMessageRequest sendMessageRequest) {
        return new SimpleMessageCreator(sendMessageRequest.getContent(), sendMessageRequest.getProperties(), messageDecorators);
    }
}
