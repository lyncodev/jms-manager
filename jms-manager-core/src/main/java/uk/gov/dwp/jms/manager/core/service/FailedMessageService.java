package uk.gov.dwp.jms.manager.core.service;

import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

public interface FailedMessageService {

    void create(FailedMessage failedMessage);

    void remove(FailedMessageId failedMessageId);
}
