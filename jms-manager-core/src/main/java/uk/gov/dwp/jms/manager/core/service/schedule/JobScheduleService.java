package uk.gov.dwp.jms.manager.core.service.schedule;

import client.FailedMessageId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.action.delay.Delay;

import java.sql.Date;

public class JobScheduleService {
    private static final String DELAYED_ACTION = "DelayedAction";
    private final ObjectMapper objectMapper;
    private final Scheduler scheduler;

    public JobScheduleService(ObjectMapper objectMapper, Scheduler scheduler) {
        this.objectMapper = objectMapper;
        this.scheduler = scheduler;
    }

    public void schedule (FailedMessageId failedMessageId, Delay delay, FailedMessageAction action) {
        try {
            scheduler.scheduleJob(TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey(failedMessageId.toString(), DELAYED_ACTION))
                    .forJob(RunActionJob.JOB_NAME, RunActionJob.JOB_GROUP)
                    .usingJobData(RunActionJob.ACTION, objectMapper.writeValueAsString(action))
                    .usingJobData(RunActionJob.MESSAGE_ID, failedMessageId.toString())
                    .startAt(Date.from(delay.triggerDate().toInstant()))
                    .build());
        } catch (JsonProcessingException | SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
