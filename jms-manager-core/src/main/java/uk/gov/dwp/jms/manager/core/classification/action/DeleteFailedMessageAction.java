package uk.gov.dwp.jms.manager.core.classification.action;

import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

@JsonTypeName("delete")
public class DeleteFailedMessageAction implements FailedMessageAction {
    @Override
    public void perform(Request request) {
        FailedMessageService failedMessageService = request.getApplicationContext().getBean(FailedMessageService.class);
        failedMessageService.remove(request.getFailedMessage());
    }
}
