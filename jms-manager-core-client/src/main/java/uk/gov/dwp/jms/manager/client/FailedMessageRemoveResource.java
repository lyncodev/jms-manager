package uk.gov.dwp.jms.manager.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message/queue/remove")
public interface FailedMessageRemoveResource {
    @POST
    @Path("/{failedMessageId}")
    void remove(@PathParam("failedMessageId") FailedMessageId messageId);

    @POST
    void remove (List<FailedMessageId> messageIds);
}
