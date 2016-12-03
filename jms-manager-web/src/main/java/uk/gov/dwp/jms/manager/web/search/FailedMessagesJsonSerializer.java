package uk.gov.dwp.jms.manager.web.search;

import client.FailedMessage;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public class FailedMessagesJsonSerializer {

    private static final DateTimeFormatter ISO_DATE_TIME_WITH_MS = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(UTC);

    private final DestinationJsonSerializer destinationJsonSerializer = new DestinationJsonSerializer();

    public String asJson(List<FailedMessage> failedMessages) {
        return failedMessages
                .stream()
                .map(fm -> new StringBuilder().append("{ ")
                        .append("recid: \"").append(fm.getFailedMessageId().getId()).append("\", ")
                        .append("content: \"").append(fm.getContent().replace("\"", "\\\"")).append("\", ")
                        .append("destination: ").append(destinationJsonSerializer.asJson(fm.getDestination())).append(", ")
                        .append("sentAt: \"").append(asJson(fm.getSentAt())).append("\", ")
                        .append("failedAt: \"").append(asJson(fm.getFailedAt())).append("\", ")
                        .append("labels: ").append(fm.getLabels().stream().collect(Collectors.joining("\", \"", "[\"", "\"]")))
                        .append("}"))
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String asJson(ZonedDateTime zonedDateTime) {
        return (zonedDateTime != null) ? ISO_DATE_TIME_WITH_MS.format(zonedDateTime) : null;
    }
}
