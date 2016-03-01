package uk.gov.dwp.jms.manager.web.search;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("/failed-messages")
@Produces(TEXT_HTML)
public class FailedMessageListController {

    private final FailedMessageResource failedMessageResource;
    private FailedMessagesJsonSerializer failedMessagesJsonSerializer;

    public FailedMessageListController(FailedMessageResource failedMessageResource, FailedMessagesJsonSerializer failedMessagesJsonSerializer) {
        this.failedMessageResource = failedMessageResource;
        this.failedMessagesJsonSerializer = failedMessagesJsonSerializer;
    }

    @GET
    public FailedMessageListPage getFailedMessages() {
        List<FailedMessage> failedMessages = failedMessageResource.getFailedMessages();
        return new FailedMessageListPage(failedMessages, failedMessagesJsonSerializer);
    }

    @GET
    @Path("/{brokerName}")
    public FailedMessageListPage getFailedMessages(@PathParam("brokerName") String brokerName) {
        // TODO: Introduce FailedMessageSearchResource in jms-manager-core
        return getFailedMessages();
    }

    @GET
    @Path("/{brokerName}/{destination}")
    public FailedMessageListPage getFailedMessages(@PathParam("brokerName") String brokerName, @PathParam("destination") String destinationName) {
        // TODO: Introduce FailedMessageSearchResource in jms-manager-core
        return getFailedMessages();
    }

}
