package uk.gov.dwp.jms.manager.core.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SendMessageRequest {
    private static final String DESTINATION = "destination";
    private static final String CONTENT = "content";
    private static final String PROPERTIES = "properties";

    @JsonProperty(DESTINATION)
    private final Destination destination;
    @JsonProperty(CONTENT)
    private final String content;
    @JsonProperty(PROPERTIES)
    private final Map<String, Object> properties;

    @JsonCreator
    public SendMessageRequest(@JsonProperty(DESTINATION) Destination destination,
                              @JsonProperty(CONTENT) String content,
                              @JsonProperty(PROPERTIES) Map<String, Object> properties) {
        this.destination = destination;
        this.content = content;
        this.properties = properties;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
