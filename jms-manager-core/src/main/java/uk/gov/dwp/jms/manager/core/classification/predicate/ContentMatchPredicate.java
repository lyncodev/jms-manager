package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Optional;

@JsonTypeName("contentMatch")
public class ContentMatchPredicate implements FailedMessagePredicate {
    private final String regex;

    @JsonCreator
    public ContentMatchPredicate(@JsonProperty("regex") String regex) {
        this.regex = regex;
    }

    @Override
    public boolean test(FailedMessage failedMessage) {
        return Optional.ofNullable(failedMessage.getContent())
                .map(content -> content.matches(regex))
                .orElse(false);
    }
}
