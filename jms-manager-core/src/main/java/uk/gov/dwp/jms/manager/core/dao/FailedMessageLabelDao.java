package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

public interface FailedMessageLabelDao {

    void save(FailedMessageLabel failedMessageLabel);

    FailedMessageLabel findById(FailedMessageId failedMessageId);

    void removeAll(FailedMessageId failedMessageId);
}
