package uk.gov.dwp.jms.manager.core.client;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.core.AllOf.allOf;

public class MatcherBuilder<B extends BaseMatcher, T> extends BaseMatcher<T> {
    private final Collection<Matcher<? super T>> matchers = new ArrayList<>();

    @Override
    public boolean matches(Object item) {
        return allOf(matchers).matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(allOf(matchers));
    }


    public B with (Matcher<? super T> matcher) {
        matchers.add(matcher);
        return (B) this;
    }
}
