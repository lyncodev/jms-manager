package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelDBObjectMapper.LABEL;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.fromString;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.newFailedMessageId;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageLabelMatcher.aFailedMessageLabel;

public class FailedMessageLabelDBObjectMapperTest {

    private static final FailedMessageId FAILED_MESSAGE_ID_VALUE = newFailedMessageId();
    private FailedMessageLabelDBObjectMapper underTest = new FailedMessageLabelDBObjectMapper();

    @Test
    public void mapFailedMessageLabel() throws Exception {
        DBObject dbObject = underTest.mapDBObject(new FailedMessageLabel(FAILED_MESSAGE_ID_VALUE, "Hello"));
        assertThat(dbObject, is(dbObject(FAILED_MESSAGE_ID_VALUE, "Hello")));
    }

    @Test
    public void testMapDBObject() throws Exception {
        FailedMessageLabel failedMessageLabel = underTest.mapObject(createFailedMessageLabelDbObject(FAILED_MESSAGE_ID_VALUE, "Hello"));

        assertThat(failedMessageLabel, is(aFailedMessageLabel()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID_VALUE))
                .withLabel(equalTo("Hello")))
        );
    }

    private BasicDBObject createFailedMessageLabelDbObject(FailedMessageId failedMessageId, String label) {
        return new BasicDBObject("_id", failedMessageId.toString()).append(LABEL, label);
    }

    private Matcher<DBObject> dbObject(final FailedMessageId failedMessageId, final String label) {
        return new TypeSafeMatcher<DBObject>() {
            @Override
            protected boolean matchesSafely(DBObject item) {
                return failedMessageId.equals(getFailedMessageId(item)) &&
                        label.equals(item.get(LABEL));
            }

            private FailedMessageId getFailedMessageId(DBObject item) {
                return fromString(((BasicDBObject)item).getString("_id"));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(FAILED_MESSAGE_ID).appendText("=").appendValue(failedMessageId).appendText(" " + LABEL).appendValue(label);
            }
        };
    }
}