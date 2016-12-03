package uk.gov.dwp.jms.manager.core.classification.action;

import client.Destination;
import client.DestinationMatcherBuilder;
import client.FailedMessage;
import client.FailedMessageId;
import client.SendMessageResource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.service.replace.StringReplacementService;

import java.util.HashMap;

import static client.SendMessageRequestMatcherBuilder.sendMessageRequest;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModifyAndSendFailedMessageActionTest {
    private final String pattern = "pattern";
    private final String replacement = "replacement";
    private final String queueDestination = "destination";
    private final ModifyAndSendFailedMessageAction underTest = new ModifyAndSendFailedMessageAction(pattern, replacement, queueDestination);

    @Test
    public void perform() throws Exception {
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
        String brokerName = "brokerName";
        String messageContent = "messageContent";
        String replacedValue = "replacedValue";
        HashMap<String, Object> properties = new HashMap<>();

        FailedMessageAction.Request request = mock(FailedMessageAction.Request.class);
        ApplicationContext applicationContext = mock(ApplicationContext.class);
        SendMessageResource sendMessageResource = mock(SendMessageResource.class);
        StringReplacementService stringReplacementService = mock(StringReplacementService.class);

        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(request.getApplicationContext()).willReturn(applicationContext);
        given(applicationContext.getBean(SendMessageResource.class)).willReturn(sendMessageResource);
        given(applicationContext.getBean(StringReplacementService.class)).willReturn(stringReplacementService);
        given(request.getFailedMessage()).willReturn(failedMessage);
        given(failedMessage.getFailedMessageId()).willReturn(failedMessageId);
        given(failedMessage.getDestination()).willReturn(destination);
        given(failedMessage.getProperties()).willReturn(properties);
        given(failedMessage.getContent()).willReturn(messageContent);
        given(destination.getBrokerName()).willReturn(brokerName);
        given(stringReplacementService.replaceRegex(messageContent, pattern, replacement)).willReturn(replacedValue);

        underTest.perform(request);

        verify(sendMessageResource).sendMessage(argThat(
                sendMessageRequest()
                    .withContent(equalTo(replacedValue))
                .withDestination(DestinationMatcherBuilder.destination().withBrokerName(equalTo(brokerName)).withQueueName(equalTo(queueDestination)))
                .withProperties(equalTo(properties))
        ));
    }

}