package uk.gov.dwp.jms.manager.core.service.resources;

import client.FailedMessageReprocessResource;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;

public class FailedMessageReprocessResourceImpl implements FailedMessageReprocessResource {
    private final FailedMessageDao failedMessageDao;
    private final FailedMessageClassifierProcessor failedMessageClassifierProcessor;

    public FailedMessageReprocessResourceImpl(FailedMessageDao failedMessageDao, FailedMessageClassifierProcessor failedMessageClassifierProcessor) {
        this.failedMessageDao = failedMessageDao;
        this.failedMessageClassifierProcessor = failedMessageClassifierProcessor;
    }

    @Override
    public void reprocess() {
        failedMessageClassifierProcessor.process(failedMessageDao::find);
    }
}
