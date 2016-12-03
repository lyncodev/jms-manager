package uk.gov.dwp.jms.manager.core.classification.predicate;

import client.FailedMessage;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AndFailedMessagePredicateTest {
    private final FailedMessagePredicate messagePredicate1 = mock(FailedMessagePredicate.class);
    private final FailedMessagePredicate messagePredicate2 = mock(FailedMessagePredicate.class);
    private final AndFailedMessagePredicate underTest = new AndFailedMessagePredicate(asList(
            messagePredicate1,
            messagePredicate2
    ));

    @Test
    public void testWhenOneReturnsTrue() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(messagePredicate1.test(failedMessage)).willReturn(false);
        given(messagePredicate2.test(failedMessage)).willReturn(true);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenBothReturnFalse() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(messagePredicate1.test(failedMessage)).willReturn(false);
        given(messagePredicate2.test(failedMessage)).willReturn(false);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenBothReturnTrue() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(messagePredicate1.test(failedMessage)).willReturn(true);
        given(messagePredicate2.test(failedMessage)).willReturn(true);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }
}