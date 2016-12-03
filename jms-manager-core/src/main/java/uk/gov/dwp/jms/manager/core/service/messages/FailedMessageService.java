package uk.gov.dwp.jms.manager.core.service.messages;

import client.FailedMessage;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

public class FailedMessageService {
    private final FailedMessageDao failedMessageDao;
    private final FailedMessageLabelsDao failedMessageLabelsDao;
    private final DestinationStatisticsDao destinationStatisticsDao;

    public FailedMessageService(FailedMessageDao failedMessageDao, FailedMessageLabelsDao failedMessageLabelsDao, DestinationStatisticsDao destinationStatisticsDao) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageLabelsDao = failedMessageLabelsDao;
        this.destinationStatisticsDao = destinationStatisticsDao;
    }

    public void remove (FailedMessage failedMessage) {
        failedMessageLabelsDao.remove(failedMessage.getFailedMessageId());
        failedMessageDao.delete(failedMessage.getFailedMessageId());
        destinationStatisticsDao.reprocess(failedMessage.getDestination());
    }

    public void create (FailedMessage failedMessage) {
        failedMessageDao.insert(failedMessage);
        destinationStatisticsDao.addFailed(failedMessage.getDestination());
    }
}
