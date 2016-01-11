package uk.gov.dwp.jms.manager.core.dao.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.newFailedMessageId;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;

public class FailedMessageMongoDaoTest extends AbstractMongoDaoTest {

    private final HashMap<String, Object> properties = new HashMap<>();
    @Autowired
    private FailedMessageMongoDao underTest;

    @Test
    public void findFailedMessageThatDoesNotExistReturnsNull() {
        assertThat(underTest.findById(newFailedMessageId()), is(nullValue(FailedMessage.class)));
    }

    @Test
    public void saveMessageWithEmptyProperties() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessage(failedMessageId, "Hello", properties));

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage()
                .withFailedMessageId(equalTo(failedMessageId))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(properties))
        ));
    }

    @Test
    public void saveMessageWithProperties() throws Exception {
        properties.put("propertyKey", "propertyValue");
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessage(failedMessageId, "Hello", properties));

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage()
                .withFailedMessageId(equalTo(failedMessageId))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(properties))
        ));
    }

    @Test
    public void attemptToRemoveAFailedMessageThatDoesNotExist() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessage(failedMessageId, "Hello", properties));

        assertThat(underTest.remove(newFailedMessageId()), is(0));

        assertThat(underTest.findById(failedMessageId), is(aFailedMessage().withFailedMessageId(equalTo(failedMessageId))));
    }

    @Test
    public void successfullyRemoveAFailedMessage() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessage(failedMessageId, "Hello", properties));

        assertThat(underTest.remove(failedMessageId), is(1));

        assertThat(underTest.findById(failedMessageId), is(nullValue()));
    }

    @Override
    protected String getCollectionName() {
        return daoProperties.getCollection().getFailedMessage();
    }
}