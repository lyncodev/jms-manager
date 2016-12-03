package uk.gov.dwp.jms.manager.core.jms;

import uk.gov.dwp.jms.manager.core.jms.activemq.ActiveMQDestinationExtractor;
import uk.gov.dwp.jms.manager.core.jms.activemq.ActiveMQFailedMessageFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

public class FailedMessageListenerFactory {
    public FailedMessageListener create (FailedMessageService failedMessageService, FailedMessageClassifierProcessor failedMessageClassifierProcessor, String brokerName) {
        return new FailedMessageListener(
                new ActiveMQFailedMessageFactory(
                        new MessageTextExtractor(),
                        new ActiveMQDestinationExtractor(brokerName),
                        new JmsMessagePropertyExtractor()),
                failedMessageService, failedMessageClassifierProcessor);
    }
}
