package uk.gov.dwp.jms.manager.core.configuration;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;
import uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelMongoDao;
import uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageMongoDao;

@Configuration
@EnableConfigurationProperties(DaoProperties.class)
public class DaoConfig {

    @Autowired
    @Bean
    public FailedMessageDao failedMessageDao(MongoClient mongoClient, DaoProperties daoProperties) {
        return new FailedMessageMongoDao(mongoClient.getDB(daoProperties.getDbName()).getCollection(daoProperties.getCollection().getFailedMessage()));
    }

    @Autowired
    @Bean
    public FailedMessageLabelDao failedMessageLabelDao(MongoClient mongoClient, DaoProperties daoProperties) {
        return new FailedMessageLabelMongoDao(mongoClient.getDB(daoProperties.getDbName()).getCollection(daoProperties.getCollection().getFailedMessageLabel()));
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
