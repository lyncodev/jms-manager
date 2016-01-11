package uk.gov.dwp.jms.manager.core.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class FailedMessageLabel {

    @NotNull
    private final FailedMessageId failedMessageId;
    @NotEmpty
    private final String label;

    public FailedMessageLabel(FailedMessageId failedMessageId, String label) {
        this.failedMessageId = failedMessageId;
        this.label = label;
    }

    public FailedMessageId getFailedMessageId() {
        return failedMessageId;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return String.format("%s[failedMessageId=%s, label=%s]", super.toString(), failedMessageId, label);
    }
}
