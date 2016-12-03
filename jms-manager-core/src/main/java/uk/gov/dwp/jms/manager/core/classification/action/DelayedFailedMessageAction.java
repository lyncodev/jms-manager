package uk.gov.dwp.jms.manager.core.classification.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.dwp.jms.manager.core.classification.action.delay.Delay;
import uk.gov.dwp.jms.manager.core.service.schedule.JobScheduleService;

@JsonTypeName("delayed")
public class DelayedFailedMessageAction implements FailedMessageAction {
    private static final String DELAY = "delay";
    private static final String ACTION = "action";

    @JsonProperty(DELAY)
    private final Delay delay;
    @JsonProperty(ACTION)
    private final FailedMessageAction action;

    @JsonCreator
    public DelayedFailedMessageAction(@JsonProperty(DELAY) Delay delay,
                                      @JsonProperty(ACTION) FailedMessageAction action) {
        this.delay = delay;
        this.action = action;
    }

    @Override
    public void perform(Request request) {
        JobScheduleService jobScheduleService = request.getApplicationContext().getBean(JobScheduleService.class);
        jobScheduleService.schedule(request.getFailedMessage().getFailedMessageId(), delay, action);
    }
}
