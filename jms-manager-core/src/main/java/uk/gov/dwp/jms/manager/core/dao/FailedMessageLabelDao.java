package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;

import java.util.Set;
import java.util.SortedSet;

public interface FailedMessageLabelDao {

    FailedMessageLabel insert(FailedMessageLabel failedMessageLabel);

    void insert(Set<FailedMessageLabel> labels);

    SortedSet<FailedMessageLabel> findById(FailedMessageId failedMessageId);

    SortedSet<FailedMessageLabel> findByLabel(String label);

    void remove(FailedMessageId failedMessageId, String label);

    int removeAll(FailedMessageId failedMessageId);
}
