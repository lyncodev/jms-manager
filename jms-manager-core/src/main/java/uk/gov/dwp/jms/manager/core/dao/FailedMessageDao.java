package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageId;

import java.util.List;

public interface FailedMessageDao {

    void insert(FailedMessage failedMessage);

    FailedMessage findById(FailedMessageId failedMessageId);

    int delete(FailedMessageId failedMessageId);

    List<FailedMessage> find();
}
