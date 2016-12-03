package uk.gov.dwp.jms.manager.core.service;

import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.dwp.jms.manager.client.Destination;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.dwp.jms.manager.core.JmsManagerApplication;
import uk.gov.dwp.jms.manager.core.configuration.DaoConfig;
import uk.gov.dwp.jms.manager.core.configuration.DaoProperties;
import uk.gov.dwp.jms.manager.core.configuration.ServicesConfiguration;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.dao.mongo.MongoTestProperties;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static uk.gov.dwp.jms.manager.client.FailedMessageBuilder.aFailedMessage;
import static java.time.ZoneOffset.UTC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {
                MongoTestProperties.class,
                DaoConfig.class,
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class
        },
        initializers = ConfigFileApplicationContextInitializer.class
)
public class CreateFailedMessagesIntTest {

    private FailedMessageService failedMessageService;

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private DaoProperties daoProperties;
    @Autowired
    private FailedMessageDao failedMessageDao;
    @Autowired
    private FailedMessageLabelsDao failedMessageLabelsDao;
    @Autowired
    private DestinationStatisticsDao destinationStatsDao;

    @Before
    public void setUp() {
        failedMessageService = new FailedMessageService(failedMessageDao, failedMessageLabelsDao, destinationStatsDao);
        clearDownDb();
    }

//    @After
//    public void tearDown() {
//        clearDownDb();
//    }

    private void clearDownDb() {
        DB db = mongoClient.getDB(daoProperties.getDbName());
        for (String name : db.getCollectionNames()) {
            if (!name.startsWith("system.")) {
                db.getCollection(name).remove(new BasicDBObject());
            }
        }
    }

    private List<Destination> destinations = Arrays.asList(
            new Destination("internal", "uc.core.queue"),
            new Destination("internal", "uc.agent.core.queue"),
            new Destination("external", "uc.cis.queue"),
            new Destination("external", "uc.rte.queue")
    );

    @Test
    public void testCreate() throws Exception {
        for (int i=0; i<10; i++) {
            failedMessageService.create(aFailedMessage()
                    .withDestination(destinations.get(i % 4))
                    .withContent("{ \"name\": \"foo\" }")
                    .withSentDateTime(ZonedDateTime.now(UTC).minusSeconds(2))
                    .withFailedDateTime(ZonedDateTime.now(UTC))
                    .build()
            );
        }
    }

}