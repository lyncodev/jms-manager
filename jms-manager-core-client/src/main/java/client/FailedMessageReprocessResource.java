package client;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Consumes("application/json")
@Produces("application/json")
@Path("/failed-message/reprocess")
public interface FailedMessageReprocessResource {
    @POST
    void reprocess ();
}
