package uk.gov.dwp.jms.manager.core.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Consumes("application/json")
@Produces("application/json")
@Path("/queue")
public interface QueueResource {
    @POST
    @Path("/send")
    void sendMessage (SendMessageRequest sendMessageRequest);
}
