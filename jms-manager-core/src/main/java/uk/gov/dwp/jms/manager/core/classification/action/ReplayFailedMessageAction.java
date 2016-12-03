package uk.gov.dwp.jms.manager.core.classification.action;

import uk.gov.dwp.jms.manager.client.FailedMessageReplayResource;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("replay")
public class ReplayFailedMessageAction implements FailedMessageAction {
    @Override
    public void perform(Request request) {
        request.getApplicationContext().getBean(FailedMessageReplayResource.class)
                .replay(request.getFailedMessage().getFailedMessageId());
    }
}
