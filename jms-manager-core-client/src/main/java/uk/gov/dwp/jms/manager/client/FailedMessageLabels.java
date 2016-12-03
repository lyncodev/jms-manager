package uk.gov.dwp.jms.manager.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FailedMessageLabels {

    @NotNull
    private final FailedMessageId failedMessageId;
    @NotEmpty
    private final SortedSet<String> labels;

    public FailedMessageLabels(@JsonProperty("failedMessageId") FailedMessageId failedMessageId,
                               @JsonProperty("labels") SortedSet<String> labels) {
        this.failedMessageId = failedMessageId;
        this.labels = labels;
    }

    public FailedMessageId getFailedMessageId() {
        return failedMessageId;
    }

    public SortedSet<String> getLabels() {
        return labels;
    }

    public static FailedMessageLabels aFailedMessageWithLabels(FailedMessageId failedMessageId, Set<String> labels) {
        return new FailedMessageLabels(failedMessageId, new TreeSet<>(labels));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailedMessageLabels that = (FailedMessageLabels) o;

        return failedMessageId.equals(that.failedMessageId) &&
                labels.equals(that.labels);
    }

    @Override
    public int hashCode() {
        return 31 * failedMessageId.hashCode() + labels.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[failedMessageId=%s, label=%s]", super.toString(), failedMessageId, labels);
    }
}
