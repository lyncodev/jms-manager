package uk.gov.dwp.jms.manager.core.jms;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;

public class FailedMessageListenerTest {

    private final FailedMessageService failedMessageService = mock(FailedMessageService.class);

    private final FailedMessageListener underTest = new FailedMessageListener(failedMessageService);

    @Test
    public void createFailedMessageFromMessageWithProperties() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("Property Key", "Property Value");
        underTest.onMessage(new MessageWithProperties("Hello", properties));

        verify(failedMessageService).create(argThat(aFailedMessage()
                .withFailedMessageId(notNullValue(FailedMessageId.class))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(properties))
        ));
    }
}