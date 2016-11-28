package uk.gov.dwp.jms.manager.core.jms.send;

import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.MessageDecorator;

import java.util.Collection;
import java.util.function.Function;

public class FailedMessageCreatorFactory implements Function<FailedMessage, MessageCreator> {
    private final Collection<MessageDecorator> messageDecorators;

    public FailedMessageCreatorFactory(Collection<MessageDecorator> messageDecorators) {
        this.messageDecorators = messageDecorators;
    }

    @Override
    public MessageCreator apply(FailedMessage failedMessage) {
        return new SimpleMessageCreator(failedMessage.getContent(), failedMessage.getProperties(), messageDecorators);
    }
}
