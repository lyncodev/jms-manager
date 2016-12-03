package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("broker")
public class BrokerPredicate implements FailedMessagePredicate {
    private static final String BROKER_NAME = "name";

    @JsonProperty(BROKER_NAME)
    private final String brokerName;

    @JsonCreator
    public BrokerPredicate(@JsonProperty(BROKER_NAME) String brokerName) {
        this.brokerName = brokerName;
    }

    @Override
    public boolean test(FailedMessage failedMessage) {
        if (failedMessage.getDestination() == null) return false;
        return brokerName.equals(failedMessage.getDestination().getBrokerName());
    }
}
