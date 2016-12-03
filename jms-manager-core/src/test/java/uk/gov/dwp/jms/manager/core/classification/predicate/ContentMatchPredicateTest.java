package uk.gov.dwp.jms.manager.core.classification.predicate;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ContentMatchPredicateTest {
    private final String regex = "abc.*";
    private ContentMatchPredicate underTest = new ContentMatchPredicate(regex);

    @Test
    public void testWhenNull() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getContent()).willReturn(null);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenNoMatch() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getContent()).willReturn(" abcd");

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenMatch() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getContent()).willReturn("abcd");

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }
}