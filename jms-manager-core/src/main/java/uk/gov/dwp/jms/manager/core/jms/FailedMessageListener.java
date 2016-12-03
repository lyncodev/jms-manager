package uk.gov.dwp.jms.manager.core.jms;

import client.FailedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class FailedMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailedMessageListener.class);

    private final FailedMessageFactory failedMessageFactory;
    private final FailedMessageService failedMessageService;
    private final FailedMessageClassifierProcessor failedMessageClassifierProcessor;

    public FailedMessageListener(FailedMessageFactory failedMessageFactory,
                                 FailedMessageService failedMessageService, FailedMessageClassifierProcessor failedMessageClassifierProcessor) {
        this.failedMessageFactory = failedMessageFactory;
        this.failedMessageService = failedMessageService;
        this.failedMessageClassifierProcessor = failedMessageClassifierProcessor;
    }

    @Override
    public void onMessage(Message message) {
        try {
            LOGGER.info("Received message: {}", message.getJMSMessageID());
            FailedMessage failedMessage = failedMessageFactory.createFailedMessage(message);
            failedMessageService.create(failedMessage);
            failedMessageClassifierProcessor.process(failedMessage);
        } catch (JMSException e) {
            LOGGER.error("Could not read jmsMessageId", e);
        }
    }
}
