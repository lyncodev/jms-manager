package uk.gov.dwp.jms.manager.core.jms;

import java.util.Map;

public class MessageWithProperties {

    private final String message;
    private final Map<String, Object> properties;

    public MessageWithProperties(String message, Map<String, Object> properties) {
        this.message = message;
        this.properties = properties;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
