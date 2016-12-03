package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.SendAttemptsDecorator;

import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SendAttemptsLowerThanPredicateTest {
    private static final int MAX_ATTEMPTS = 3;
    private final SendAttemptsLowerThanPredicate underTest = new SendAttemptsLowerThanPredicate(MAX_ATTEMPTS);

    @Test
    public void testWhenNoProperty() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(new HashMap());

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }

    @Test
    public void testWhenPropertyNotInteger() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(SendAttemptsDecorator.PROPERTY, "abc"));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }

    @Test
    public void testWhenPropertyIntegerAndLower() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(SendAttemptsDecorator.PROPERTY, MAX_ATTEMPTS - 1));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }

    @Test
    public void testWhenPropertyIntegerAndEqual() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(SendAttemptsDecorator.PROPERTY, MAX_ATTEMPTS));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenPropertyIntegerAndGreater() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(SendAttemptsDecorator.PROPERTY, MAX_ATTEMPTS + 1));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }
}