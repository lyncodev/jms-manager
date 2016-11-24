package uk.gov.dwp.jms.manager.core.jms;

import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.jms.activemq.ActiveMQDestinationExtractor;
import uk.gov.dwp.jms.manager.core.jms.activemq.ActiveMQFailedMessageFactory;

public class FailedMessageListenerFactory {
    public FailedMessageListener create (FailedMessageResource failedMessageResource, String brokerName) {
        return new FailedMessageListener(
                new ActiveMQFailedMessageFactory(
                        new MessageTextExtractor(),
                        new ActiveMQDestinationExtractor(brokerName),
                        new JmsMessagePropertyExtractor()),
                failedMessageResource);
    }
}
