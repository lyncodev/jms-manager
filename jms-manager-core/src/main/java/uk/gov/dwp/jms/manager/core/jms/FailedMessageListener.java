package uk.gov.dwp.jms.manager.core.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class FailedMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailedMessageListener.class);

    private final FailedMessageFactory failedMessageFactory;
    private final FailedMessageResource failedMessageResource;

    public FailedMessageListener(FailedMessageFactory failedMessageFactory,
                                 FailedMessageResource failedMessageResource) {
        this.failedMessageFactory = failedMessageFactory;
        this.failedMessageResource = failedMessageResource;
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.info("Received message: {}", message.getJMSMessageID());
            failedMessageResource.create(failedMessageFactory.createFailedMessage(message));
        } catch (JMSException e) {
            LOGGER.error("Could not read jmsMessageId", e);
        }
    }
}
