package uk.gov.dwp.jms.manager.core.jms.activemq;

import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageBuilder;
import uk.gov.dwp.jms.manager.core.jms.DestinationExtractor;
import uk.gov.dwp.jms.manager.core.jms.FailedMessageFactory;
import uk.gov.dwp.jms.manager.core.jms.MessagePropertyExtractor;
import uk.gov.dwp.jms.manager.core.jms.MessageTextExtractor;

import javax.jms.Message;
import java.time.Instant;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

public class ActiveMQFailedMessageFactory implements FailedMessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQFailedMessageFactory.class);

    private final MessageTextExtractor messageTextExtractor;
    private final DestinationExtractor<ActiveMQMessage> destinationExtractor;
    private final MessagePropertyExtractor messagePropertyExtractor;

    public ActiveMQFailedMessageFactory(MessageTextExtractor messageTextExtractor,
                                        DestinationExtractor<ActiveMQMessage> destinationExtractor,
                                        MessagePropertyExtractor messagePropertyExtractor) {
        this.messageTextExtractor = messageTextExtractor;
        this.destinationExtractor = destinationExtractor;
        this.messagePropertyExtractor = messagePropertyExtractor;
    }

    @Override
    public FailedMessage createFailedMessage(Message message) {
        validateMessageOfCorrectType(message);
        ActiveMQMessage activeMQMessage = (ActiveMQMessage) message;
        return FailedMessageBuilder.aFailedMessage()
                .withContent(messageTextExtractor.extractText(message))
                .withDestination(destinationExtractor.extractDestination(activeMQMessage))
                .withSentDateTime(extractTimestamp(activeMQMessage.getTimestamp()))
                .withFailedDateTime(extractTimestamp(activeMQMessage.getBrokerInTime()))
                .withProperties(messagePropertyExtractor.extractProperties(activeMQMessage))
                .withJmsMessageId(activeMQMessage.getJMSMessageID())
                .build();
    }

    private void validateMessageOfCorrectType(Message in) {
        if (!(in instanceof ActiveMQMessage)) {
            String errorMessage;
            if (in == null) {
                errorMessage = "Message cannot be null";
            } else {
                errorMessage = "Expected ActiveMQMessage received: " + in.getClass().getName();
            }
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private ZonedDateTime extractTimestamp(long ms) {
        return (ms != 0) ? ZonedDateTime.from(ZonedDateTime.ofInstant(Instant.ofEpochMilli(ms), UTC)) : null;
    }
}
