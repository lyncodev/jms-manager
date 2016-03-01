package uk.gov.dwp.jms.manager.core.jms.activemq;

import org.apache.activemq.command.ActiveMQMessage;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.jms.DestinationExtractor;

public class ActiveMQDestinationExtractor implements DestinationExtractor<ActiveMQMessage> {

    private final String brokerName;

    public ActiveMQDestinationExtractor(String brokerName) {
        this.brokerName = brokerName;
    }

    @Override
    public Destination extractDestination(ActiveMQMessage message) {
        return new Destination(brokerName, message.getOriginalDestination().getPhysicalName());
    }
}
