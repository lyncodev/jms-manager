package uk.gov.dwp.jms.manager.core.domain;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class FailedMessageMatcher extends TypeSafeMatcher<FailedMessage> {

    private Matcher<FailedMessageId> failedMessageIdMatcher = notNullValue(FailedMessageId.class);
    private Matcher<String> contentMatcher = notNullValue(String.class);
    private Matcher<Map<String, Object>> propertiesMatcher = equalTo(new HashMap<>());

    private FailedMessageMatcher() { }

    public static FailedMessageMatcher aFailedMessage() {
        return new FailedMessageMatcher();
    }

    public FailedMessageMatcher withFailedMessageId(Matcher<FailedMessageId> failedMessageIdMatcher) {
        this.failedMessageIdMatcher = failedMessageIdMatcher;
        return this;
    }

    public FailedMessageMatcher withContent(Matcher<String> contentMatcher) {
        this.contentMatcher = contentMatcher;
        return this;
    }

    public FailedMessageMatcher withProperties(Matcher<Map<String, Object>> propertiesMatcher) {
        this.propertiesMatcher = propertiesMatcher;
        return this;
    }

    @Override
    protected boolean matchesSafely(FailedMessage item) {
        return failedMessageIdMatcher.matches(item.getFailedMessageId()) &&
                contentMatcher.matches(item.getContent()) &&
                propertiesMatcher.matches(item.getProperties());
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("failedMessageId is ").appendDescriptionOf(failedMessageIdMatcher)
                .appendText(" content is ").appendDescriptionOf(contentMatcher)
                .appendText(" properties are ").appendDescriptionOf(propertiesMatcher);
    }
}