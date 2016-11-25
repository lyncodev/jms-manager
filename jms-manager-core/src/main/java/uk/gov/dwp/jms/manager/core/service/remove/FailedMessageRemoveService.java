package uk.gov.dwp.jms.manager.core.service.remove;

import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

public class FailedMessageRemoveService {
    private final FailedMessageDao failedMessageDao;
    private final FailedMessageLabelsDao failedMessageLabelsDao;
    private final DestinationStatisticsDao destinationStatisticsDao;

    public FailedMessageRemoveService(FailedMessageDao failedMessageDao, FailedMessageLabelsDao failedMessageLabelsDao, DestinationStatisticsDao destinationStatisticsDao) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageLabelsDao = failedMessageLabelsDao;
        this.destinationStatisticsDao = destinationStatisticsDao;
    }

    public void remove (FailedMessage failedMessage) {
        failedMessageLabelsDao.remove(failedMessage.getFailedMessageId());
        failedMessageDao.delete(failedMessage.getFailedMessageId());
        destinationStatisticsDao.reprocess(failedMessage.getDestination());
    }
}
