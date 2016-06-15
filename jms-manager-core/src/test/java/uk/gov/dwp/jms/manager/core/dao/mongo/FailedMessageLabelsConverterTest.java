package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabels;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.fromString;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.newFailedMessageId;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelsConverter.LABELS;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageLabelsMatcher.aFailedMessageWithLabels;

public class FailedMessageLabelsConverterTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = newFailedMessageId();
    private FailedMessageLabelsConverter underTest = new FailedMessageLabelsConverter();

    @Test
    public void mapFailedMessageLabelToDBObjectAndBack() throws Exception {
        FailedMessageLabels failedMessageLabel = new FailedMessageLabels(FAILED_MESSAGE_ID, treeSet("Hello"));

        BasicDBObject dbObject = underTest.convertFromObject(failedMessageLabel);

        assertThat(dbObject, is(dbObject(equalTo(FAILED_MESSAGE_ID), contains("Hello"))));
        assertThat(underTest.convertToObject(dbObject), is(aFailedMessageWithLabels()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID))
                .withLabels(contains("Hello")))
        );
    }

    private SortedSet<String> treeSet(String label) {
        return new TreeSet<String>() {{
            add(label);
        }};
    }

    private Matcher<BasicDBObject> dbObject(final Matcher<FailedMessageId> failedMessageIdMatcher, final Matcher<Iterable<? extends String>> labelsMatcher) {
        return new TypeSafeMatcher<BasicDBObject>() {
            @Override
            protected boolean matchesSafely(BasicDBObject item) {
                return failedMessageIdMatcher.matches(fromString(((BasicDBObject)item.get("_id")).getString(FailedMessageId.FAILED_MESSAGE_ID))) &&
                        labelsMatcher.matches(item.get(LABELS));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(FailedMessageId.FAILED_MESSAGE_ID)
                        .appendDescriptionOf(failedMessageIdMatcher)
                        .appendText(" " + LABELS + " is ")
                        .appendDescriptionOf(labelsMatcher);
            }
        };
    }
}