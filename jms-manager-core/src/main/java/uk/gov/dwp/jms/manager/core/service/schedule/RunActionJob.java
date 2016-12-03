package uk.gov.dwp.jms.manager.core.service.schedule;

import uk.gov.dwp.jms.manager.client.FailedMessageId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;

import java.io.IOException;
import java.util.Optional;

public class RunActionJob implements Job {
    public static final String JOB_NAME = RunActionJob.class.getSimpleName();
    public static final String JOB_GROUP = "RunActionGroup";
    public static final String ACTION = "action";
    public static final String MESSAGE_ID = "messageId";

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        FailedMessageDao failedMessageDao = applicationContext.getBean(FailedMessageDao.class);
        FailedMessageClassifierProcessor failedMessageClassifierProcessor = applicationContext.getBean(FailedMessageClassifierProcessor.class);

        FailedMessageAction action = getAction(context, objectMapper);
        FailedMessageId failedMessageId = getFailedMessageId(context);
        failedMessageClassifierProcessor.performAction(
                () -> Optional.ofNullable(failedMessageDao.findById(failedMessageId)),
                action
        );
    }

    private FailedMessageId getFailedMessageId(JobExecutionContext context) {
        return FailedMessageId.fromString(context.getMergedJobDataMap().getString(MESSAGE_ID));
    }

    private FailedMessageAction getAction(JobExecutionContext context, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(context.getMergedJobDataMap().getString(ACTION), FailedMessageAction.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
