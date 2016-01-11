package uk.gov.dwp.jms.manager.core.dao.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.dwp.jms.manager.core.configuration.DaoConfig;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DaoConfig.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class},
        initializers = ConfigFileApplicationContextInitializer.class)
public class FailedMessageLabelMongoDaoTest {

    @Autowired
    private FailedMessageLabelMongoDao underTest;

    @Test
    public void canCreateMultipleLabels() throws Exception {


    }
}