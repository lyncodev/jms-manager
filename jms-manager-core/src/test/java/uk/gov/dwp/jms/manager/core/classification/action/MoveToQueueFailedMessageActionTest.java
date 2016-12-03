package uk.gov.dwp.jms.manager.core.classification.action;

import client.Destination;
import client.FailedMessage;
import client.FailedMessageId;
import client.FailedMessageMoveResource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.client.DestinationMatcherBuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MoveToQueueFailedMessageActionTest {
    private final String moveDestination = "destination";
    private MoveToQueueFailedMessageAction underTest = new MoveToQueueFailedMessageAction(moveDestination);

    @Test
    public void perform() throws Exception {
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
        String brokerName = "brokerName";

        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        FailedMessageMoveResource failedMessageMoveResource = mock(FailedMessageMoveResource.class);
        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(request.getApplicationContext()).willReturn(applicationContext);
        given(applicationContext.getBean(FailedMessageMoveResource.class)).willReturn(failedMessageMoveResource);
        given(request.getFailedMessage()).willReturn(failedMessage);
        given(failedMessage.getFailedMessageId()).willReturn(failedMessageId);
        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(brokerName);

        underTest.perform(request);

        verify(failedMessageMoveResource).move(eq(failedMessageId), argThat(DestinationMatcherBuilder.destination()
                .withBrokerName(equalTo(brokerName))
                .withQueueName(equalTo(moveDestination))
        ));
    }
}