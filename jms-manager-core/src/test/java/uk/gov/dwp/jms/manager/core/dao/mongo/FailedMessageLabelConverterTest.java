package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.DBObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.newFailedMessageId;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelConverter.LABEL;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageLabelMatcher.aFailedMessageLabel;

public class FailedMessageLabelConverterTest {

    private static final FailedMessageId FAILED_MESSAGE_ID_VALUE = newFailedMessageId();
    private FailedMessageLabelConverter underTest = new FailedMessageLabelConverter();

    @Test
    public void mapFailedMessageLabelToDBObjectAndBack() throws Exception {
        FailedMessageLabel failedMessageLabel = new FailedMessageLabel(FAILED_MESSAGE_ID_VALUE, "Hello");

        DBObject dbObject = underTest.convertFromObject(failedMessageLabel);

        assertThat(dbObject, is(dbObject(FAILED_MESSAGE_ID_VALUE, "Hello")));
        assertThat(underTest.convertToObject(dbObject), is(aFailedMessageLabel()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID_VALUE))
                .withLabel(equalTo("Hello")))
        );
    }

    private Matcher<DBObject> dbObject(final FailedMessageId failedMessageId, final String label) {
        return new TypeSafeMatcher<DBObject>() {
            @Override
            protected boolean matchesSafely(DBObject item) {
                return failedMessageId.getId().toString().equals(item.get(FAILED_MESSAGE_ID)) &&
                        label.equals(item.get(LABEL));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(FAILED_MESSAGE_ID).appendText("=").appendValue(failedMessageId).appendText(" " + LABEL + " is ").appendValue(label);
            }
        };
    }
}