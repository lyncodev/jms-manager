package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;
import uk.gov.dwp.jms.manager.core.service.FailedMessageServiceImpl;

@Configuration
@Import(DaoConfig.class)
public class ServiceConfig {

    @Bean
    public FailedMessageService failedMessageService(FailedMessageDao failedMessageDao,
                                                     FailedMessageLabelsDao failedMessageLabelsDao,
                                                     DestinationStatisticsDao destinationStatisticsDao) {
        return new FailedMessageServiceImpl(failedMessageDao, failedMessageLabelsDao, destinationStatisticsDao);
    }
}
