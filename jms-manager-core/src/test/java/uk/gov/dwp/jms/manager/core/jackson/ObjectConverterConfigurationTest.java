package uk.gov.dwp.jms.manager.core.jackson;

import client.Destination;
import client.FailedMessage;
import client.FailedMessageBuilder;
import client.FailedMessageId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.configuration.JacksonConfiguration;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static client.FailedMessageId.newFailedMessageId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;
import static uk.gov.dwp.jms.manager.core.util.HashMapBuilder.newHashMap;

public class ObjectConverterConfigurationTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = newFailedMessageId();
    private final JacksonConfiguration jacksonConfiguration = new JacksonConfiguration();

    @Test
    public void testName() throws Exception {
        ObjectMapper objectMapper = jacksonConfiguration.objectMapper();
        HashMap<String, Object> properties = createProperties();
        FailedMessage failedMessage = FailedMessageBuilder.aFailedMessage()
                .withFailedMessageId(FAILED_MESSAGE_ID)
                .withDestination(new Destination("broker.name", "queue.name"))
                .withSentDateTime(ZonedDateTime.now())
                .withFailedDateTime(ZonedDateTime.now())
                .withContent("Hello")
                .withProperties(properties)
                .build();
        String output = objectMapper.writeValueAsString(failedMessage);
        FailedMessage actual = objectMapper.readValue(output, FailedMessage.class);
        assertThat(actual,
                is(aFailedMessage()
                        .withFailedMessageId(equalTo(FAILED_MESSAGE_ID))
                        .withContent(equalTo("Hello"))
                        .withProperties(equalTo(properties)))
        );
    }

    private HashMap<String, Object> createProperties() {
        return newHashMap(String.class, Object.class)
                    .put("string", "some text")
                    .put("localDateTime", LocalDateTime.now())
                    .put("date", new Date())
                    .put("integer", 1)
                    .put("long", 1L)
                    .put("uuid", UUID.randomUUID())
                    .build();
    }
}