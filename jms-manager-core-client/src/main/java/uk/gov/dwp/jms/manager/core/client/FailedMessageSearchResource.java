package uk.gov.dwp.jms.manager.core.client;

import javax.ws.rs.*;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message/search")
public interface FailedMessageSearchResource {

    @GET
    @Path("/{failedMessageId}")
    FailedMessage getFailedMessage(@PathParam("failedMessageId") FailedMessageId failedMessageId);

    @GET
    List<FailedMessage> findMessages(SearchRequest searchRequest);
}
