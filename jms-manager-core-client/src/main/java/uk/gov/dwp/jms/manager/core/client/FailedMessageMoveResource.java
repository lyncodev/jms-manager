package uk.gov.dwp.jms.manager.core.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message/queue/move")
public interface FailedMessageMoveResource {
    @POST
    @Path("/{failedMessageId}")
    void move(@PathParam("failedMessageId") FailedMessageId messageId, Destination destination);

    @POST
    void move(Bulk bulk);


    class Bulk {
        private static final String MESSAGE_IDS = "messageIds";
        private static final String DESTINATION = "destination";

        @JsonProperty(MESSAGE_IDS)
        private final List<FailedMessageId> messageIds;
        @JsonProperty(DESTINATION)
        private final Destination destination;

        public Bulk(@JsonProperty(MESSAGE_IDS) List<FailedMessageId> messageIds,
                    @JsonProperty(DESTINATION) Destination destination) {
            this.messageIds = messageIds;
            this.destination = destination;
        }

        public List<FailedMessageId> getMessageIds() {
            return messageIds;
        }

        public Destination getDestination() {
            return destination;
        }
    }
}
