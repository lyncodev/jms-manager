package uk.gov.dwp.jms.manager.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Set;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message")
public interface FailedMessageResource {

    @Deprecated
    @GET
    @Path("/{failedMessageId}")
    FailedMessage getFailedMessage(@PathParam("failedMessageId") FailedMessageId failedMessgeId);

    @PUT
    @Path("/{failedMessageId}/add/{label}")
    void addLabel(@PathParam("failedMessageId") FailedMessageId failedMessageId, @PathParam("label") String label);

    @PUT
    @Path("/{failedMessageId}/add")
    void setLabels(@PathParam("failedMessageId") FailedMessageId failedMessageId, Set<String> labels);

    @Deprecated
    @GET
    @Path("/all")
    List<FailedMessage> getFailedMessages();
}
