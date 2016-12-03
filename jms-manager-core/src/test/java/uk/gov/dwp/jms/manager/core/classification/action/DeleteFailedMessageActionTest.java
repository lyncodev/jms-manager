package uk.gov.dwp.jms.manager.core.classification.action;

import client.FailedMessage;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteFailedMessageActionTest {
    private DeleteFailedMessageAction underTest = new DeleteFailedMessageAction();

    @Test
    public void perform() throws Exception {
        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        FailedMessageService failedMessageService = mock(FailedMessageService.class);
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(request.getApplicationContext()).willReturn(applicationContext);
        given(applicationContext.getBean(FailedMessageService.class)).willReturn(failedMessageService);
        given(request.getFailedMessage()).willReturn(failedMessage);

        underTest.perform(request);

        verify(failedMessageService).remove(failedMessage);
    }
}