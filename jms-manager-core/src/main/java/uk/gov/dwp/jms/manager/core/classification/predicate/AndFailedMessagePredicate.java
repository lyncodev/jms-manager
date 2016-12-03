package uk.gov.dwp.jms.manager.core.classification.predicate;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collection;

@JsonTypeName("and")
public class AndFailedMessagePredicate implements FailedMessagePredicate {
    private static final String PREDICATES = "predicates";

    @JsonProperty(PREDICATES)
    private final Collection<FailedMessagePredicate> messageClassifiers;

    @JsonCreator
    public AndFailedMessagePredicate(@JsonProperty(PREDICATES) Collection<FailedMessagePredicate> messageClassifiers) {
        this.messageClassifiers = messageClassifiers;
    }

    @Override
    public boolean test(FailedMessage failedMessage) {
        for (FailedMessagePredicate predicate : messageClassifiers) {
            if (!predicate.test(failedMessage)) return false;
        }

        return true;
    }
}
