package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;
import uk.gov.dwp.jms.manager.core.service.FailedMessageServiceImpl;

@Configuration
@Import(DaoConfig.class)
public class ServiceConfig {

    @Bean
    @Autowired
    public FailedMessageService failedMessageService(FailedMessageDao failedMessageDao, FailedMessageLabelDao failedMessageLabelDao) {
        return new FailedMessageServiceImpl(failedMessageDao, failedMessageLabelDao);
    }
}
