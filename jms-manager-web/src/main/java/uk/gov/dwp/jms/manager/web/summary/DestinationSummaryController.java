package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.core.client.DestinationStatisticsResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/summary")
@Produces(TEXT_HTML)
public class DestinationSummaryController {

    private final DestinationStatisticsResource destinationStatisticsResource;
    private final DestinationSummaryJsonSerializer destinationSummaryJsonSerializer;

    public DestinationSummaryController(DestinationStatisticsResource destinationStatisticsResource, DestinationSummaryJsonSerializer destinationSummaryJsonSerializer) {
        this.destinationStatisticsResource = destinationStatisticsResource;
        this.destinationSummaryJsonSerializer = destinationSummaryJsonSerializer;
    }

    @GET
    public DestinationSummaryPage getSummary() {
        return new DestinationSummaryPage(
                destinationSummaryJsonSerializer.asJson(destinationStatisticsResource.getAllStatistics())
        );
    }

}
