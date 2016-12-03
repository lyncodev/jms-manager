package uk.gov.dwp.jms.manager.core.classification.action;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CompositeFailedMessageActionTest {
    private final FailedMessageAction failedMessageAction = mock(FailedMessageAction.class);
    private final CompositeFailedMessageAction underTest = new CompositeFailedMessageAction(asList(failedMessageAction));


    @Test
    public void perform() throws Exception {
        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);

        underTest.perform(request);

        verify(failedMessageAction).perform(request);
    }
}