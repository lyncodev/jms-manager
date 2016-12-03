package client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class DelayedSendMessageRequest {
    private static final String DATE_TIME = "dateTime";
    private static final String MESSAGE_REQUEST = "messageRequest";

    @JsonProperty(DATE_TIME)
    private final ZonedDateTime dateTime;
    @JsonProperty(MESSAGE_REQUEST)
    private final SendMessageRequest messageRequest;

    public DelayedSendMessageRequest(@JsonProperty(DATE_TIME) ZonedDateTime dateTime, @JsonProperty(MESSAGE_REQUEST) SendMessageRequest messageRequest) {
        this.dateTime = dateTime;
        this.messageRequest = messageRequest;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public SendMessageRequest getMessageRequest() {
        return messageRequest;
    }
}
