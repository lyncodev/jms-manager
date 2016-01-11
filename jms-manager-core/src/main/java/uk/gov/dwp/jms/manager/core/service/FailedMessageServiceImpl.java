package uk.gov.dwp.jms.manager.core.service;

import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

public class FailedMessageServiceImpl implements FailedMessageService {

    private final FailedMessageDao failedMessageDao;
    private final FailedMessageLabelDao failedMessageLabelDao;

    public FailedMessageServiceImpl(FailedMessageDao failedMessageDao, FailedMessageLabelDao failedMessageLabelDao) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageLabelDao = failedMessageLabelDao;
    }

    @Override
    public void create(FailedMessage failedMessage) {
        failedMessageDao.insert(failedMessage);
    }

    @Override
    public void remove(FailedMessageId failedMessageId) {
        failedMessageLabelDao.removeAll(failedMessageId);
        failedMessageDao.remove(failedMessageId);
    }
}
