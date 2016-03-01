package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.core.client.DestinationStatisticsResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/summary")
@Produces(TEXT_HTML)
public class DestinationStatisticsController {

    private final DestinationStatisticsResource destinationStatisticsResource;
    private final DestinationStatisticsJsonSerializer destinationStatisticsJsonSerializer;

    public DestinationStatisticsController(DestinationStatisticsResource destinationStatisticsResource, DestinationStatisticsJsonSerializer destinationStatisticsJsonSerializer) {
        this.destinationStatisticsResource = destinationStatisticsResource;
        this.destinationStatisticsJsonSerializer = destinationStatisticsJsonSerializer;
    }

    @GET
    public DestinationStatisticsPage getSummary() {
        return new DestinationStatisticsPage(
                destinationStatisticsJsonSerializer.asJson(destinationStatisticsResource.getAllStatistics())
        );
    }

}
