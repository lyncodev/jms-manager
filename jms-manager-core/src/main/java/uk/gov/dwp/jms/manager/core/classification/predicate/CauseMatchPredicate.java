package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Optional;

@JsonTypeName("causeMatch")
public class CauseMatchPredicate implements FailedMessagePredicate {
    public static final String PROPERTY_NAME = "dlqDeliveryFailureCause";
    private final String regex;

    @JsonCreator
    public CauseMatchPredicate(@JsonProperty("regex") String regex) {
        this.regex = regex;
    }


    @Override
    public boolean test(FailedMessage failedMessage) {
        return Optional.ofNullable(failedMessage.getProperties().get(PROPERTY_NAME))
                .filter(String.class::isInstance)
                .map(x -> (String) x)
                .map(x -> x.matches(regex))
                .orElse(false);
    }
}
