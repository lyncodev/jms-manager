package uk.gov.dwp.jms.manager.core.classification.action.delay;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@JsonTypeName("sumRelative")
public class SumRelativeDelay implements Delay {
    private static final String LIST = "list";

    @JsonProperty(LIST)
    private final Collection<RelativeDelay> list;

    @JsonCreator
    public SumRelativeDelay(@JsonProperty(LIST) Collection<RelativeDelay> list) {
        this.list = list;
    }

    @Override
    public ZonedDateTime triggerDate() {
        long total = list.stream().mapToLong(x -> x.getUnit().toMillis(x.getDuration())).sum();
        return ZonedDateTime.now().plus(total, ChronoUnit.MILLIS);
    }
}
