package uk.gov.dwp.jms.manager.core.classification.action.delay;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.ZonedDateTime;

@JsonTypeName("absolute")
public class AbsoluteDelay implements Delay {
    private static final String DATE_TIME = "dateTime";

    @JsonProperty(DATE_TIME)
    private final ZonedDateTime dateTime;

    @JsonCreator
    public AbsoluteDelay(@JsonProperty(DATE_TIME) ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public ZonedDateTime triggerDate() {
        return dateTime;
    }
}
