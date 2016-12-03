package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageBuilder;
import uk.gov.dwp.jms.manager.client.FailedMessageId;
import com.mongodb.BasicDBObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.jms.manager.core.util.HashMapBuilder;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static uk.gov.dwp.jms.manager.client.FailedMessageId.newFailedMessageId;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;
import static uk.gov.dwp.jms.manager.core.util.HashMapBuilder.newHashMap;

public class FailedMessageMongoDaoTest extends AbstractMongoDaoTest {

    private final FailedMessageId failedMessageId = newFailedMessageId();
    private final FailedMessageBuilder failedMessageBuilder = FailedMessageBuilder.aFailedMessage()
            .withFailedMessageId(failedMessageId)
            .withDestination(new Destination("broker", "queue.name"))
            .withContent("Hello")
            .withSentDateTime(ZonedDateTime.now())
            .withFailedDateTime(ZonedDateTime.now());

    @Autowired
    private FailedMessageMongoDao underTest;

    @Test
    public void findFailedMessageThatDoesNotExistReturnsNull() {
        assertThat(underTest.findById(newFailedMessageId()), is(nullValue(FailedMessage.class)));
    }

    @Test
    public void saveMessageWithEmptyProperties() throws Exception {
        failedMessageBuilder.withProperties(emptyMap());

        underTest.insert(failedMessageBuilder.build());

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage()
                .withFailedMessageId(equalTo(failedMessageId))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(emptyMap()))
        ));
    }

    @Test
    public void saveMessageWithProperties() throws Exception {
        HashMapBuilder<String, Object> hashMapBuilder = newHashMap(String.class, Object.class)
                .put("string", "Builder")
                .put("localDateTime", LocalDateTime.now())
                .put("date", new Date())
                .put("integer", 1)
                .put("long", 1L)
                .put("uuid", UUID.randomUUID());

        HashMap<String, Object> properties = hashMapBuilder.build();
        properties.put("properties", hashMapBuilder.build());
        failedMessageBuilder.withProperties(properties);

        underTest.insert(failedMessageBuilder.build());

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage()
                .withFailedMessageId(equalTo(failedMessageId))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(properties))
        ));
    }

    @Test
    public void attemptToDeleteAFailedMessageThatDoesNotExist() throws Exception {
        underTest.insert(failedMessageBuilder.build());

        assertThat(underTest.delete(newFailedMessageId()), is(0));

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage()
                .withFailedMessageId(equalTo(failedMessageId))));
    }

    @Test
    public void successfullyDeleteAFailedMessage() throws Exception {
        underTest.insert(failedMessageBuilder.build());

        assertThat(underTest.delete(failedMessageId), is(1));
        assertThat(underTest.findById(failedMessageId), is(nullValue()));
        assertThat(collection.count(new BasicDBObject("_removed._id", failedMessageId.toString())), is(1L));
    }

    @Override
    protected String getCollectionName() {
        return daoProperties.getCollection().getFailedMessage();
    }
}