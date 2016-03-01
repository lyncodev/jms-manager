package uk.gov.dwp.jms.manager.core.service;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;

import java.util.List;

public class FailedMessageServiceImpl implements FailedMessageService, FailedMessageResource {

    private final FailedMessageDao failedMessageDao;
    private final DestinationStatisticsDao destinationStatisticsDao;
    private final FailedMessageLabelDao failedMessageLabelDao;

    public FailedMessageServiceImpl(FailedMessageDao failedMessageDao, FailedMessageLabelDao failedMessageLabelDao, DestinationStatisticsDao destinationStatisticsDao) {
        this.failedMessageDao = failedMessageDao;
        this.destinationStatisticsDao = destinationStatisticsDao;
        this.failedMessageLabelDao = failedMessageLabelDao;
    }

    @Override
    public void create(FailedMessage failedMessage) {
        failedMessageDao.insert(failedMessage);
        destinationStatisticsDao.addFailed(failedMessage.getDestination());
    }

    @Override
    public void reprocess(FailedMessageId failedMessageId) {
        FailedMessage failedMessage = failedMessageDao.findById(failedMessageId);
        failedMessageLabelDao.removeAll(failedMessageId);
        failedMessageDao.remove(failedMessageId);
        destinationStatisticsDao.reprocess(failedMessage.getDestination());
    }

    @Override
    public FailedMessage getFailedMessage(FailedMessageId failedMessgeId) {
        return failedMessageDao.findById(failedMessgeId);
    }

    @Override
    public void addLabel(FailedMessageId failedMessageId, String label) {
        failedMessageLabelDao.insert(new FailedMessageLabel(failedMessageId, label));
    }

    @Override
    public List<FailedMessage> getFailedMessages() {
        return failedMessageDao.find();
    }
}
