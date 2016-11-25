package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.service.remove.FailedMessageRemoveService;

@Configuration
public class ServicesConfiguration {
    @Bean
    public FailedMessageRemoveService failedMessageRemoveService(FailedMessageDao failedMessageDao, FailedMessageLabelsDao failedMessageLabelsDao, DestinationStatisticsDao destinationStatisticsDao) {
        return new FailedMessageRemoveService(failedMessageDao, failedMessageLabelsDao, destinationStatisticsDao);
    }

}
