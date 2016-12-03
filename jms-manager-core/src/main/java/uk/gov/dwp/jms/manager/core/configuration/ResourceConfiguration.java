package uk.gov.dwp.jms.manager.core.configuration;

import client.DestinationStatisticsResource;
import client.FailedMessageMoveResource;
import client.FailedMessageRemoveResource;
import client.FailedMessageReplayResource;
import client.FailedMessageReprocessResource;
import client.FailedMessageResource;
import client.FailedMessageSearchResource;
import client.SendMessageResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.jms.send.FailedMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.jms.send.SendMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;
import uk.gov.dwp.jms.manager.core.service.resources.DestinationStatisticsResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageMoveResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageRemoveResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageReplayResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageReprocessResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageSearchResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.SendMessageResourceImpl;

@Configuration
@Import({ DaoConfig.class })
public class ResourceConfiguration {

    @Bean
    public FailedMessageSearchResource failedMessageSearchResource(FailedMessageDao failedMessageDao,
                                                                   FailedMessageLabelsDao failedMessageLabelsDao) {

        return new FailedMessageSearchResourceImpl(failedMessageDao, failedMessageLabelsDao);
    }

    @Bean
    public DestinationStatisticsResource destinationStatisticsResource(DestinationStatisticsDao destinationStatisticsDao) {
        return new DestinationStatisticsResourceImpl(destinationStatisticsDao);
    }

    @Bean
    public FailedMessageResource failedMessageResource(FailedMessageDao failedMessageDao,
                                                       FailedMessageLabelsDao failedMessageLabelsDao,
                                                       DestinationStatisticsDao destinationStatisticsDao) {
        return new FailedMessageResourceImpl(failedMessageDao, failedMessageLabelsDao);
    }

    @Bean
    public FailedMessageRemoveResource failedMessageRemoveResource (FailedMessageDao failedMessageDao, FailedMessageService failedMessageService) {
        return new FailedMessageRemoveResourceImpl(failedMessageDao, failedMessageService);
    }

    @Bean
    public FailedMessageReplayResource failedMessageReplayResource (FailedMessageDao failedMessageDao, FailedMessageService failedMessageService, MessageSenderFactory messageSenderFactory, FailedMessageCreatorFactory failedMessageCreatorFactory) {
        return new FailedMessageReplayResourceImpl(failedMessageDao, messageSenderFactory, failedMessageCreatorFactory, failedMessageService);
    }

    @Bean
    public FailedMessageMoveResource failedMessageMoveResource (FailedMessageDao failedMessageDao, FailedMessageService failedMessageService, MessageSenderFactory messageSenderFactory, FailedMessageCreatorFactory failedMessageCreatorFactory) {
        return new FailedMessageMoveResourceImpl(failedMessageDao, messageSenderFactory, failedMessageCreatorFactory, failedMessageService);
    }

    @Bean
    public SendMessageResource sendMessageResource (MessageSenderFactory messageSenderFactory, SendMessageCreatorFactory sendMessageCreatorFactory, Scheduler scheduler, ObjectMapper objectMapper) {
        return new SendMessageResourceImpl(messageSenderFactory, sendMessageCreatorFactory, scheduler, objectMapper);
    }

    @Bean
    public FailedMessageReprocessResource failedMessageReprocessResource (FailedMessageDao failedMessageDao, FailedMessageClassifierProcessor listener) {
        return new FailedMessageReprocessResourceImpl(failedMessageDao, listener);
    }
}
