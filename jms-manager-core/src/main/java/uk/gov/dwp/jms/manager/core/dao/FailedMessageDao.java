package uk.gov.dwp.jms.manager.core.dao;

import client.FailedMessage;
import client.FailedMessageId;

import java.util.List;

public interface FailedMessageDao {

    void insert(FailedMessage failedMessage);

    FailedMessage findById(FailedMessageId failedMessageId);

    int delete(FailedMessageId failedMessageId);

    List<FailedMessage> find();
}
