package uk.gov.dwp.jms.manager.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;
import uk.gov.dwp.jms.manager.core.service.replace.StringReplacementService;
import uk.gov.dwp.jms.manager.core.service.schedule.JobScheduleService;

@Configuration
public class ServicesConfiguration {
    @Bean
    public FailedMessageService failedMessageRemoveService(FailedMessageDao failedMessageDao, FailedMessageLabelsDao failedMessageLabelsDao, DestinationStatisticsDao destinationStatisticsDao) {
        return new FailedMessageService(failedMessageDao, failedMessageLabelsDao, destinationStatisticsDao);
    }

    @Bean
    public StringReplacementService stringReplaceService () {
        return StringReplacementService.stringReplaceService();
    }

    @Bean
    public JobScheduleService jobScheduleService (ObjectMapper objectMapper, Scheduler scheduler) {
        return new JobScheduleService(objectMapper, scheduler);
    }
}
