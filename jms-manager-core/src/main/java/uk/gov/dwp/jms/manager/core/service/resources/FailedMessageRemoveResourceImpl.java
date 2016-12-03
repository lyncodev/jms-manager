package uk.gov.dwp.jms.manager.core.service.resources;

import client.FailedMessageId;
import client.FailedMessageRemoveResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import java.util.List;

public class FailedMessageRemoveResourceImpl implements FailedMessageRemoveResource {
    private final FailedMessageDao failedMessageDao;
    private final FailedMessageService failedMessageService;

    public FailedMessageRemoveResourceImpl(FailedMessageDao failedMessageDao, FailedMessageService failedMessageService) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageService = failedMessageService;
    }

    @Override
    public void remove(FailedMessageId failedMessageId) {
        failedMessageService.remove(failedMessageDao.findById(failedMessageId));
    }

    @Override
    public void remove(List<FailedMessageId> messageIds) {
        messageIds.forEach(this::remove);
    }

}
