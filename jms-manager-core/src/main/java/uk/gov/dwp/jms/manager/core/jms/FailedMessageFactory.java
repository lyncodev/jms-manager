package uk.gov.dwp.jms.manager.core.jms;

import uk.gov.dwp.jms.manager.client.FailedMessage;

import javax.jms.Message;

public interface FailedMessageFactory {

    FailedMessage createFailedMessage(Message message);
}
