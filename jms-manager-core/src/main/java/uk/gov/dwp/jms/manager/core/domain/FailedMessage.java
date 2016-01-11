package uk.gov.dwp.jms.manager.core.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class FailedMessage {

    @NotNull
    private final FailedMessageId failedMessageId;
    @NotEmpty
    private final String content;
    @NotNull
    private final Map<String, Object> properties;

    public FailedMessage(FailedMessageId failedMessageId, String content, Map<String, Object> properties) {
        this.failedMessageId = failedMessageId;
        this.content = content;
        this.properties = properties;
    }

    public FailedMessageId getFailedMessageId() {
        return failedMessageId;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
