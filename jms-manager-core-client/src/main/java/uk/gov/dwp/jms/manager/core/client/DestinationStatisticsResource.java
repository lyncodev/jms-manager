package uk.gov.dwp.jms.manager.core.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Consumes("application/json")
@Produces("application/json")
@Path("/destination/statistics")
public interface DestinationStatisticsResource {

    @GET
    @Path("/all")
    List<DestinationStatistics> getAllStatistics();
}
