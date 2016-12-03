package uk.gov.dwp.jms.manager.core.classification.action;

import client.FailedMessage;
import client.FailedMessageId;
import client.FailedMessageReplayResource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReplayFailedMessageActionTest {
    private ReplayFailedMessageAction underTest = new ReplayFailedMessageAction();

    @Test
    public void perform() throws Exception {
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();

        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        FailedMessageReplayResource failedMessageReplayResource = mock(FailedMessageReplayResource.class);
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(request.getApplicationContext()).willReturn(applicationContext);
        given(applicationContext.getBean(FailedMessageReplayResource.class)).willReturn(failedMessageReplayResource);
        given(request.getFailedMessage()).willReturn(failedMessage);
        given(failedMessage.getFailedMessageId()).willReturn(failedMessageId);

        underTest.perform(request);

        verify(failedMessageReplayResource).replay(failedMessageId);
    }
}