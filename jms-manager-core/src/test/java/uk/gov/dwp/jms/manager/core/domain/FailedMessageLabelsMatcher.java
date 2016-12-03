package uk.gov.dwp.jms.manager.core.domain;

import client.FailedMessageId;
import client.FailedMessageLabels;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.notNullValue;

public class FailedMessageLabelsMatcher extends TypeSafeMatcher<FailedMessageLabels> {

    private Matcher<FailedMessageId> failedMessageIdMatcher = notNullValue(FailedMessageId.class);
    private Matcher<Iterable<? extends String>> labelsMatcher = emptyIterable();

    private FailedMessageLabelsMatcher() { }

    public static FailedMessageLabelsMatcher aFailedMessageWithLabels() {
        return new FailedMessageLabelsMatcher();
    }

    public FailedMessageLabelsMatcher withFailedMessageId(Matcher<FailedMessageId> failedMessageIdMatcher) {
        this.failedMessageIdMatcher = failedMessageIdMatcher;
        return this;
    }

    public FailedMessageLabelsMatcher withLabels(Matcher<Iterable<? extends String>> labelsMatcher) {
        this.labelsMatcher = labelsMatcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(FailedMessageLabels item) {
        return failedMessageIdMatcher.matches(item.getFailedMessageId()) &&
                labelsMatcher.matches(item.getLabels());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("failedMessageId is ").appendDescriptionOf(failedMessageIdMatcher)
                .appendText(" labels are ").appendDescriptionOf(labelsMatcher);
    }
}