package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabels;

import java.util.Set;
import java.util.SortedSet;

public interface FailedMessageLabelsDao {

    void addLabel(FailedMessageId failedMessageId, String label);

    void setLabels(FailedMessageId failedMessageId, Set<String> labels);

    SortedSet<String> findLabelsById(FailedMessageId failedMessageId);

    Set<FailedMessageLabels> findByLabel(String label);

    int remove(FailedMessageId failedMessageId);
}
