package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.SendAttemptsDecorator;

@JsonTypeName("sendAttemptsLowerThan")
public class SendAttemptsLowerThanPredicate implements FailedMessagePredicate {
    private static final String MAX_ATTEMPTS = "maxAttempts";

    @JsonProperty(MAX_ATTEMPTS)
    private final int maxAttempts;

    public SendAttemptsLowerThanPredicate(@JsonProperty(MAX_ATTEMPTS) int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public boolean test(FailedMessage failedMessage) {
        return getAttemptsProperty(failedMessage) < maxAttempts;
    }

    private int getAttemptsProperty(FailedMessage failedMessage) {
        try {
            Object propertyObject = failedMessage.getProperties().get(SendAttemptsDecorator.PROPERTY);
            return Integer.parseInt(propertyObject.toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
