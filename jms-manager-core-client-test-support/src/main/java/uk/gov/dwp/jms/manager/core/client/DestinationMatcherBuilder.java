package uk.gov.dwp.jms.manager.core.client;

import uk.gov.dwp.jms.manager.client.Destination;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

public class DestinationMatcherBuilder extends MatcherBuilder<DestinationMatcherBuilder, Destination> {
    public static DestinationMatcherBuilder destination () {
        return new DestinationMatcherBuilder();
    }

    public DestinationMatcherBuilder withBrokerName (Matcher<String> matcher) {
        return with(new FeatureMatcher<Destination, String>(matcher, "brokerName", "brokerName") {
            @Override
            protected String featureValueOf(Destination actual) {
                return actual.getBrokerName();
            }
        });
    }

    public DestinationMatcherBuilder withQueueName (Matcher<String> matcher) {
        return with(new FeatureMatcher<Destination, String>(matcher, "queueName", "queueName") {
            @Override
            protected String featureValueOf(Destination actual) {
                return actual.getName();
            }
        });
    }
}
