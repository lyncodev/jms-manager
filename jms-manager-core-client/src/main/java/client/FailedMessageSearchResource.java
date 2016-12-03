package client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
