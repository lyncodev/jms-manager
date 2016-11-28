package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.core.client.DestinationStatisticsResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageMoveResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageRemoveResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageReplayResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageSearchResource;
import uk.gov.dwp.jms.manager.core.client.QueueResource;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.jms.send.FailedMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;
import uk.gov.dwp.jms.manager.core.jms.send.SendMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;
import uk.gov.dwp.jms.manager.core.service.resources.DestinationStatisticsResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageMoveResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageRemoveResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageReplayResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageSearchResourceImpl;
import uk.gov.dwp.jms.manager.core.service.resources.QueueResourceImpl;

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
        return new FailedMessageResourceImpl(failedMessageDao, failedMessageLabelsDao, destinationStatisticsDao);
    }

    @Bean
    public FailedMessageRemoveResource failedMessageRemoveResource (FailedMessageDao failedMessageDao, FailedMessageRemoveService failedMessageRemoveService) {
        return new FailedMessageRemoveResourceImpl(failedMessageDao, failedMessageRemoveService);
    }

    @Bean
    public FailedMessageReplayResource failedMessageReplayResource (FailedMessageDao failedMessageDao, FailedMessageRemoveService failedMessageRemoveService, MessageSenderFactory messageSenderFactory, FailedMessageCreatorFactory failedMessageCreatorFactory) {
        return new FailedMessageReplayResourceImpl(failedMessageDao, messageSenderFactory, failedMessageCreatorFactory, failedMessageRemoveService);
    }

    @Bean
    public FailedMessageMoveResource failedMessageMoveResource (FailedMessageDao failedMessageDao, FailedMessageRemoveService failedMessageRemoveService, MessageSenderFactory messageSenderFactory, FailedMessageCreatorFactory failedMessageCreatorFactory) {
        return new FailedMessageMoveResourceImpl(failedMessageDao, messageSenderFactory, failedMessageCreatorFactory, failedMessageRemoveService);
    }

    @Bean
    public QueueResource queueResource (MessageSenderFactory messageSenderFactory, SendMessageCreatorFactory sendMessageCreatorFactory) {
        return new QueueResourceImpl(messageSenderFactory, sendMessageCreatorFactory);
    }
}
