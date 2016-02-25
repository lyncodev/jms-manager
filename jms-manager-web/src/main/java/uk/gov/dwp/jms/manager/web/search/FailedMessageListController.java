package uk.gov.dwp.jms.manager.web.search;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/failed-messages")
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

}
