package uk.gov.dwp.jms.manager.core.classification.action;

import client.FailedMessage;
import client.FailedMessageId;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LabelFailedMessageActionTest {
    private final String labelName = "labelName";
    private LabelFailedMessageAction underTest = new LabelFailedMessageAction(labelName);

    @Test
    public void perform() throws Exception {
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();

        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        FailedMessageLabelsDao failedMessageLabelsDao = mock(FailedMessageLabelsDao.class);
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(request.getApplicationContext()).willReturn(applicationContext);
        given(applicationContext.getBean(FailedMessageLabelsDao.class)).willReturn(failedMessageLabelsDao);
        given(request.getFailedMessage()).willReturn(failedMessage);
        given(failedMessage.getFailedMessageId()).willReturn(failedMessageId);

        underTest.perform(request);

        verify(failedMessageLabelsDao).addLabel(failedMessageId, labelName);
    }
}