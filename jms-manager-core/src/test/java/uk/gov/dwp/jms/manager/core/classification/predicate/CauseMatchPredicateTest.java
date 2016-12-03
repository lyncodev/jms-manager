package uk.gov.dwp.jms.manager.core.classification.predicate;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CauseMatchPredicateTest {
    private final String regex = "abc.*";
    private CauseMatchPredicate underTest = new CauseMatchPredicate(regex);

    @Test
    public void testWhenNoCauseExists() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.emptyMap());

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenCauseExistsAndNull() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(CauseMatchPredicate.PROPERTY_NAME, null));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenCauseExistsAndNotString() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(CauseMatchPredicate.PROPERTY_NAME, 1));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenCauseExistsAndNotMatches() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(CauseMatchPredicate.PROPERTY_NAME, " abcd"));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenCauseExistsAndMatches() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getProperties()).willReturn(Collections.singletonMap(CauseMatchPredicate.PROPERTY_NAME, "abcd"));

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }
}