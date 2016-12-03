package uk.gov.dwp.jms.manager.core.client;

import client.Destination;
import client.SendMessageRequest;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import java.util.Map;

public class SendMessageRequestMatcherBuilder extends MatcherBuilder<SendMessageRequestMatcherBuilder, SendMessageRequest> {
    public static SendMessageRequestMatcherBuilder sendMessageRequest () {
        return new SendMessageRequestMatcherBuilder();
    }

    public SendMessageRequestMatcherBuilder withContent (Matcher<String> matcher) {
        return with(new FeatureMatcher<SendMessageRequest, String>(matcher, "content", "content") {
            @Override
            protected String featureValueOf(SendMessageRequest actual) {
                return actual.getContent();
            }
        });
    }

    public SendMessageRequestMatcherBuilder withDestination (Matcher<Destination> matcher) {
        return with(new FeatureMatcher<SendMessageRequest, Destination>(matcher, "destination", "destination") {
            @Override
            protected Destination featureValueOf(SendMessageRequest actual) {
                return actual.getDestination();
            }
        });
    }

    public SendMessageRequestMatcherBuilder withProperties (Matcher<Map<String, Object>> matcher) {
        return with(new FeatureMatcher<SendMessageRequest, Map<String, Object>>(matcher, "properties", "properties") {
            @Override
            protected Map<String, Object> featureValueOf(SendMessageRequest actual) {
                return actual.getProperties();
            }
        });
    }
}
