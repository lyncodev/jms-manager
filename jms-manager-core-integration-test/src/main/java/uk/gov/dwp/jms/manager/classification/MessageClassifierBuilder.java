package uk.gov.dwp.jms.manager.classification;

import org.apache.commons.lang3.builder.Builder;
import uk.gov.dwp.jms.manager.core.classification.MessageClassifier;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.predicate.FailedMessagePredicate;

public class MessageClassifierBuilder implements Builder<MessageClassifier> {
    public static MessageClassifierBuilder messageClassifierBuilder () {
        return new MessageClassifierBuilder();
    }

    private FailedMessagePredicate predicate;
    private FailedMessageAction action;
    private Boolean continueChain;

    public MessageClassifierBuilder withPredicate(FailedMessagePredicate predicate) {
        this.predicate = predicate;
        return this;
    }

    public MessageClassifierBuilder withAction(FailedMessageAction action) {
        this.action = action;
        return this;
    }

    public MessageClassifierBuilder withContinueChain(Boolean continueChain) {
        this.continueChain = continueChain;
        return this;
    }

    @Override
    public MessageClassifier build() {
        return new MessageClassifier(predicate, action, continueChain);
    }
}
