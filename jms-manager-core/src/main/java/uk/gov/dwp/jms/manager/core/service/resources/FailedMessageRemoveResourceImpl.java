package uk.gov.dwp.jms.manager.core.service.resources;

import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageRemoveResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

import java.util.List;

public class FailedMessageRemoveResourceImpl implements FailedMessageRemoveResource {
    private final FailedMessageDao failedMessageDao;
    private final FailedMessageRemoveService failedMessageRemoveService;

    public FailedMessageRemoveResourceImpl(FailedMessageDao failedMessageDao, FailedMessageRemoveService failedMessageRemoveService) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageRemoveService = failedMessageRemoveService;
    }

    @Override
    public void remove(FailedMessageId failedMessageId) {
        failedMessageRemoveService.remove(failedMessageDao.findById(failedMessageId));
    }

    @Override
    public void remove(List<FailedMessageId> messageIds) {
        messageIds.forEach(this::remove);
    }

}
