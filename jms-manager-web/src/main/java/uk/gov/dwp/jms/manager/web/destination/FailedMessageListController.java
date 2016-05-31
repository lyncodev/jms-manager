package uk.gov.dwp.jms.manager.web.destination;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.web.w2ui.BaseW2UIRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;
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

    @POST
    @Path("/delete")
    @Consumes("application/json")
    public String deleteFailedMessages(BaseW2UIRequest request) {
        List<FailedMessageId> failedMessageIds = request.getSelected().stream().map(FailedMessageId::fromString).collect(toList());
        failedMessageResource.delete(failedMessageIds);
        return "{ 'status': 'success' }";
    }

    @POST
    @Path("/add-label")
    @Consumes("application/json")
    public String addLabelToFailedMessages(AddLabelRequest request) {
        request.getChanges().stream().forEach(change -> failedMessageResource.addLabel(change.));
        return "{ 'status': 'success' }";
    }


    @POST
    @Path("/resend")
    @Consumes(MediaType.APPLICATION_JSON)
    public String resendFailedMessages(BaseW2UIRequest request) {
        // Resend
        return  "{ 'status': 'success' }";
    }

    @POST
    @Path("/data")
    @Consumes(MediaType.APPLICATION_JSON)
    public String getData(BaseW2UIRequest request) {
        List<FailedMessage> failedMessages = failedMessageResource.getFailedMessages();
        return getJson(failedMessages);
    }

    private String getJson(List<FailedMessage> failedMessages) {
        return new StringBuilder("{ ")
                .append("'status': 'success'")
                .append(", 'total': ").append(failedMessages.size())
                .append(", 'records': ").append(failedMessagesJsonSerializer.asJson(failedMessages))
                .append(" }")
                .toString();
    }
}
