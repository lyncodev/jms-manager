package uk.gov.dwp.jms.manager.core.classification.action.delay;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@JsonTypeName("relative")
public class RelativeDelay implements Delay {
    private static final String UNIT = "unit";
    private static final String DURATION = "duration";

    @JsonProperty(UNIT)
    private final TimeUnit unit;
    @JsonProperty(DURATION)
    private final int duration;

    @JsonCreator
    public RelativeDelay(@JsonProperty(UNIT) TimeUnit unit, @JsonProperty(DURATION) int duration) {
        this.unit = unit;
        this.duration = duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public ZonedDateTime triggerDate() {
        return ZonedDateTime.now().plus(unit.toMillis(duration), ChronoUnit.MILLIS);
    }
}
