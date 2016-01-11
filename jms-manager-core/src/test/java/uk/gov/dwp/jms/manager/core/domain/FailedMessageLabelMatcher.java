package uk.gov.dwp.jms.manager.core.domain;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class FailedMessageLabelMatcher extends TypeSafeMatcher<FailedMessageLabel> {

    private Matcher<FailedMessageId> failedMessageIdMatcher = notNullValue(FailedMessageId.class);
    private Matcher<String> labelMatcher = notNullValue(String.class);

    private FailedMessageLabelMatcher() { }

    public static FailedMessageLabelMatcher aFailedMessageLabel() {
        return new FailedMessageLabelMatcher();
    }

    public FailedMessageLabelMatcher withFailedMessageId(Matcher<FailedMessageId> failedMessageIdMatcher) {
        this.failedMessageIdMatcher = failedMessageIdMatcher;
        return this;
    }

    public FailedMessageLabelMatcher withLabel(Matcher<String> labelMatcher) {
        this.labelMatcher = labelMatcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(FailedMessageLabel item) {
        return failedMessageIdMatcher.matches(item.getFailedMessageId()) &&
                labelMatcher.matches(item.getLabel());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("failedMessageId is ").appendDescriptionOf(failedMessageIdMatcher)
                .appendText(" label is ").appendDescriptionOf(labelMatcher);
    }
}