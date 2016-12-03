package uk.gov.dwp.jms.manager.core.classification.action;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.FailedMessageMoveResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("moveToQueue")
public class MoveToQueueFailedMessageAction implements FailedMessageAction {
    private static final String DESTINATION_QUEUE = "destination";

    @JsonProperty(DESTINATION_QUEUE)
    private final String destination;

    @JsonCreator
    public MoveToQueueFailedMessageAction(@JsonProperty(DESTINATION_QUEUE) String destination) {
        this.destination = destination;
    }

    @Override
    public void perform(Request request) {
        request.getApplicationContext().getBean(FailedMessageMoveResource.class)
                .move(request.getFailedMessage().getFailedMessageId(), new Destination(
                        request.getFailedMessage().getDestination().getBrokerName(),
                        destination
                ));
    }
}
