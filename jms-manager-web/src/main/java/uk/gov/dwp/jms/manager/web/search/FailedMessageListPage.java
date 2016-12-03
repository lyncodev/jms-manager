package uk.gov.dwp.jms.manager.web.search;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.web.common.Page;

import java.util.List;

public class FailedMessageListPage extends Page {

    private final List<FailedMessage> failedMessages;
    private final FailedMessagesJsonSerializer failedMessagesJsonSerializer;

    public FailedMessageListPage(List<FailedMessage> failedMessages, FailedMessagesJsonSerializer failedMessagesJsonSerializer) {
        super("failed-message-list.mustache");
        this.failedMessages = failedMessages;
        this.failedMessagesJsonSerializer = failedMessagesJsonSerializer;
    }

    public List<FailedMessage> getFailedMessages() {
        return failedMessages;
    }

    public String getFailedMessagesAsJson() {
        return failedMessagesJsonSerializer.asJson(failedMessages);
    }
}
