package uk.gov.dwp.jms.manager.core.client;

import javax.ws.rs.*;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message")
public interface FailedMessageResource {

    @GET
    @Path("/{failedMessageId}")
    FailedMessage getFailedMessage(@PathParam("failedMessageId") FailedMessageId failedMessgeId);

    @PUT
    @Path("/{failedMessageId}/{label}")
    void addLabel(@PathParam("failedMessageId") FailedMessageId failedMessageId, @PathParam("label") String label);

    @GET
    @Path("/all")
    List<FailedMessage> getFailedMessages();
}
