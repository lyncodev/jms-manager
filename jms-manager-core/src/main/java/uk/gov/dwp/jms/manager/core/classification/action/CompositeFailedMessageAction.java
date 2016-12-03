package uk.gov.dwp.jms.manager.core.classification.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Collection;

@JsonTypeName("composite")
public class CompositeFailedMessageAction implements FailedMessageAction {
    private static final String ACTIONS = "actions";

    @JsonProperty(ACTIONS)
    private final Collection<FailedMessageAction> actions;

    public CompositeFailedMessageAction(@JsonProperty(ACTIONS) Collection<FailedMessageAction> actions) {
        this.actions = actions;
    }

    @Override
    public void perform(Request request) {
        for (FailedMessageAction action : actions) {
            action.perform(request);
        }
    }
}
