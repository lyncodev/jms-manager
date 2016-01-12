package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

import java.util.List;

public interface FailedMessageLabelDao {

    FailedMessageLabel insert(FailedMessageLabel failedMessageLabel);

    List<FailedMessageLabel> findById(FailedMessageId failedMessageId);

    List<FailedMessageLabel> findByLabel(String label);

    void remove(FailedMessageId failedMessageId, String label);

    int removeAll(FailedMessageId failedMessageId);
}
