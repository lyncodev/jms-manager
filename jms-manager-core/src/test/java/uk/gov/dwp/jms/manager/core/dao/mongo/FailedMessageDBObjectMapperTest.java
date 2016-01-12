package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageDBObjectMapper.CONTENT;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageDBObjectMapper.PROPERTIES;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.newFailedMessageId;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;

public class FailedMessageDBObjectMapperTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = newFailedMessageId();
    private static final String FAILED_MESSAGE_ID_AS_STRING = FAILED_MESSAGE_ID.getId().toString();
    private static final BasicDBObject _ID = new BasicDBObject("_id", FAILED_MESSAGE_ID.getId().toString());
    private static final Map<String, Object> EMPTY_PROPERTIES = new HashMap<>();
    private static final Map<String, Object> SOME_PROPERTIES = new HashMap<String, Object>() {{
        put("propertyName", "propertyValue");
    }};

    private FailedMessageDBObjectMapper underTest = new FailedMessageDBObjectMapper();

    @Test
    public void createId() {
        assertThat(underTest.createFailedMessageIdDBObject(FAILED_MESSAGE_ID), equalTo(new BasicDBObject("_id", FAILED_MESSAGE_ID_AS_STRING)));
    }

    @Test
    public void mapFailedMessageWithNoPropertiesToDBObject() {
        DBObject dbObject = underTest.mapDBObject(new FailedMessage(FAILED_MESSAGE_ID, "Hello", EMPTY_PROPERTIES));
        assertThat(dbObject, is(dbObject("Hello", EMPTY_PROPERTIES)));
    }

    @Test
    public void mapFailedMessageWithPropertiesToDBObject() {
        DBObject dbObject = underTest.mapDBObject(new FailedMessage(FAILED_MESSAGE_ID, "Hello", SOME_PROPERTIES));
        assertThat(dbObject, is(dbObject("Hello", SOME_PROPERTIES)));
    }

    @Test
    public void mapNullDBObjectToFailedMessage() {
        assertThat(underTest.mapObject(null), is(nullValue()));
    }

    @Test
    public void mapDBObjectWithNoPropertiesToFailedMessage() {
        FailedMessage message = underTest.mapObject(createFailedMessageDbObject(EMPTY_PROPERTIES));

        assertThat(message, is(aFailedMessage()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(EMPTY_PROPERTIES)))
        );
    }

    @Test
    public void mapDBObjectWithPropertiesToFailedMessage() {
        FailedMessage message = underTest.mapObject(createFailedMessageDbObject(SOME_PROPERTIES));

        assertThat(message, is(aFailedMessage()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID))
                .withContent(equalTo("Hello"))
                .withProperties(equalTo(SOME_PROPERTIES))));
    }

    private BasicDBObject createFailedMessageDbObject(Map<String, Object> properties) {
        return _ID.append(CONTENT, "Hello").append(PROPERTIES, properties);
    }

    private Matcher<DBObject> dbObject(final String message, final Map<String, Object> properties) {
        return new TypeSafeMatcher<DBObject>() {
            @Override
            protected boolean matchesSafely(DBObject item) {
                return FAILED_MESSAGE_ID_AS_STRING.equals(item.get("_id")) &&
                        message.equals(item.get(CONTENT)) &&
                        properties.equals(item.get(PROPERTIES));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(CONTENT).appendText("=").appendText(message).appendText(" " + PROPERTIES).appendValue(properties);
            }
        };
    }
}