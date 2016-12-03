package uk.gov.dwp.jms.manager.core.service.resources;

import client.DelayedSendMessageRequest;
import client.SendMessageRequest;
import client.SendMessageResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.service.schedule.SendMessageJob;

import java.util.Date;
import java.util.function.Function;

public class SendMessageResourceImpl implements SendMessageResource {
    private final MessageSenderFactory messageSenderFactory;
    private final Function<SendMessageRequest, MessageCreator> messageCreatorFunction;
    private final Scheduler scheduler;
    private final ObjectMapper objectMapper;

    public SendMessageResourceImpl(MessageSenderFactory messageSenderFactory, Function<SendMessageRequest, MessageCreator> messageCreatorFunction, Scheduler scheduler, ObjectMapper objectMapper) {
        this.messageSenderFactory = messageSenderFactory;
        this.messageCreatorFunction = messageCreatorFunction;
        this.scheduler = scheduler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMessage(SendMessageRequest sendMessageRequest) {
        messageSenderFactory
                .senderFor(sendMessageRequest.getDestination().getBrokerName())
                .send(sendMessageRequest.getDestination().getName(), messageCreatorFunction.apply(sendMessageRequest));
    }

    @Override
    public void sendMessage(DelayedSendMessageRequest request) {
        try {
            scheduler.scheduleJob(TriggerBuilder.newTrigger()
                    .forJob(SendMessageJob.JOB_NAME, SendMessageJob.JOB_GROUP)
                    .usingJobData(SendMessageJob.REQUEST, objectMapper.writeValueAsString(request.getMessageRequest()))
                    .startAt(Date.from(request.getDateTime().toInstant()))
                    .build());
        } catch (SchedulerException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
