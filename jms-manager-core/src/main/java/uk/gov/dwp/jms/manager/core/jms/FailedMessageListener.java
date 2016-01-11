package uk.gov.dwp.jms.manager.core.jms;

import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.newFailedMessageId;

public class FailedMessageListener implements MessageWithPropertiesListener {

    private final FailedMessageService failedMessageService;

    public FailedMessageListener(FailedMessageService failedMessageService) {
        this.failedMessageService = failedMessageService;
    }

    @Override
    public void onMessage(MessageWithProperties message) {
        failedMessageService.create(new FailedMessage(newFailedMessageId(), message.getMessage(), message.getProperties()));
    }
}
