package uk.gov.dwp.jms.manager.core.jms;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashMap;
import java.util.Map;

public class MessageWithProperties {

    private final String message;
    @NotEmpty
    private final Map<String, Object> properties;

    public MessageWithProperties(String message, Map<String, Object> properties) {
        this.message = message;
        this.properties = properties;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageWithProperties that = (MessageWithProperties) o;

        return (message == null ? that.message == null : message.equals(that.message)) && properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return 31 * (message != null ? message.hashCode() : 0) + properties.hashCode();
    }
}
