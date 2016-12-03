package client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message/queue/replay")
public interface FailedMessageReplayResource {
    @POST
    @Path("/{failedMessageId}")
    void replay(@PathParam("failedMessageId") FailedMessageId messageId);

    @POST
    void replay (List<FailedMessageId> messageIds);
}
