package uk.gov.dwp.jms.manager.core.classification;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.predicate.FailedMessagePredicate;


public class MessageClassifier {
    private static final String PREDICATE = "predicate";
    private static final String ACTION = "action";
    private static final String INTERRUPT_CHAIN = "continueChain";

    @JsonProperty(PREDICATE)
    private final FailedMessagePredicate predicate;
    @JsonProperty(ACTION)
    private final FailedMessageAction action;
    @JsonProperty(INTERRUPT_CHAIN)
    private final Boolean continueChain;

    public MessageClassifier(@JsonProperty(PREDICATE) FailedMessagePredicate predicate,
                             @JsonProperty(ACTION) FailedMessageAction action,
                             @JsonProperty(INTERRUPT_CHAIN) Boolean continueChain) {
        this.predicate = predicate;
        this.action = action;
        this.continueChain = continueChain;
    }

    public FailedMessagePredicate getPredicate() {
        return predicate;
    }

    public FailedMessageAction getAction() {
        return action;
    }

    public boolean continueChain () {
        return Boolean.TRUE.equals(continueChain);
    }
}
