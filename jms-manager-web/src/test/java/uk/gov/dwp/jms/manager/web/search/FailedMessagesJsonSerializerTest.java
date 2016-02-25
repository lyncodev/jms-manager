package uk.gov.dwp.jms.manager.web.search;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageBuilder;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.valid4j.matchers.jsonpath.JsonPathMatchers.hasJsonPath;

public class FailedMessagesJsonSerializerTest {

    private static final ZonedDateTime EPOCH = ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.of(0, 0, 0), UTC);
    private static final ZonedDateTime SOME_DATE_TIME = ZonedDateTime.of(LocalDate.of(2016, 2, 8), LocalTime.of(14, 43, 0), UTC);
    private static final String FAILED_MESSAGE_ID_1 = UUID.randomUUID().toString();
    private static final String FAILED_MESSAGE_ID_2 = UUID.randomUUID().toString();
    private final FailedMessagesJsonSerializer underTest = new FailedMessagesJsonSerializer();

    @Test
    public void failedMessageWithNoProperties() {
        FailedMessage failedMessage1 = FailedMessageBuilder.aFailedMessage()
                .withFailedMessageId(FailedMessageId.fromString(FAILED_MESSAGE_ID_1))
                .withSentDateTime(EPOCH)
                .withFailedDateTime(SOME_DATE_TIME)
                .withContent("Some Content")
                .build();
        FailedMessage failedMessage2 = FailedMessageBuilder.aFailedMessage()
                .withFailedMessageId(FailedMessageId.fromString(FAILED_MESSAGE_ID_2))
                .withSentDateTime(SOME_DATE_TIME.withZoneSameInstant(ZoneOffset.ofHours(2)))
                .withFailedDateTime(SOME_DATE_TIME.with(MILLI_OF_SECOND, 123))
                .withContent("More Content")
                .build();
        String json = underTest.asJson("failedMessages", Arrays.asList(failedMessage1, failedMessage2));

        assertThat(json, allOf(
                hasJsonPath("$.failedMessages[0].recid", equalTo(FAILED_MESSAGE_ID_1)),
                hasJsonPath("$.failedMessages[0].content", equalTo("Some Content")),
                hasJsonPath("$.failedMessages[0].sentAt", equalTo("1970-01-01T00:00:00.000Z")),
                hasJsonPath("$.failedMessages[0].failedAt", equalTo("2016-02-08T14:43:00.000Z")),

                hasJsonPath("$.failedMessages[1].recid", equalTo(FAILED_MESSAGE_ID_2)),
                hasJsonPath("$.failedMessages[1].content", equalTo("More Content")),
                hasJsonPath("$.failedMessages[1].sentAt", equalTo("2016-02-08T14:43:00.000Z")),
                hasJsonPath("$.failedMessages[1].failedAt", equalTo("2016-02-08T14:43:00.123Z"))
        ));

    }
}