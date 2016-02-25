package uk.gov.dwp.jms.manager.core.service;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;

public interface FailedMessageService {

    void create(FailedMessage failedMessage);

    void remove(FailedMessageId failedMessageId);
}
