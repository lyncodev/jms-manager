package uk.gov.dwp.jms.manager.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class DestinationStatistics {

    private final Destination destination;
    private final long failed;
    private final long reprocessed;
    private final long deleted;

    public DestinationStatistics(@JsonProperty("destination") Destination destination,
                                 @JsonProperty("failed") long failed,
                                 @JsonProperty("reprocessed") long reprocessed,
                                 @JsonProperty("deleted") long deleted) {
        this.destination = destination;
        this.failed = failed;
        this.reprocessed = reprocessed;
        this.deleted = deleted;
    }

    public Destination getDestination() {
        return destination;
    }

    public long getFailed() {
        return failed;
    }

    public long getReprocessed() {
        return reprocessed;
    }

    public long getDeleted() {
        return deleted;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
