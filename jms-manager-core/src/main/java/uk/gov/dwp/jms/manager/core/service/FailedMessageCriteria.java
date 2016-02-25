package uk.gov.dwp.jms.manager.core.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class FailedMessageCriteria {

    private final Set<String> labels;

    public FailedMessageCriteria(@JsonProperty("labels") Set<String> labels) {
        this.labels = labels;
    }

    public Set<String> getLabels() {
        return labels;
    }
}
