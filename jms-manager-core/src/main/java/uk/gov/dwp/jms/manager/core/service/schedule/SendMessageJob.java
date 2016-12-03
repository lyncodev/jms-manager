package uk.gov.dwp.jms.manager.core.service.schedule;


import client.SendMessageRequest;
import client.SendMessageResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class SendMessageJob implements Job {
    public static final String JOB_NAME = SendMessageJob.class.getSimpleName();
    public static final String JOB_GROUP = "sendMessage";
    public static final String REQUEST = "request";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SendMessageResource sendMessageResource;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SendMessageRequest sendMessageRequest = objectMapper.readValue(context.getTrigger().getJobDataMap().getString(REQUEST), SendMessageRequest.class);
            sendMessageResource.sendMessage(sendMessageRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
