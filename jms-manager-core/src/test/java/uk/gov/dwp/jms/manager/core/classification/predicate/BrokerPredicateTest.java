package uk.gov.dwp.jms.manager.core.classification.predicate;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.FailedMessage;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BrokerPredicateTest {
    private final String brokerName = "name";
    private BrokerPredicate underTest = new BrokerPredicate(brokerName);

    @Test
    public void testWhenNotSameBroker() throws Exception {
        String broker = "anotherBroker";
        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(broker);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenSameBroker() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(brokerName);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(true));
    }

    @Test
    public void testWhenNullBroker() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(failedMessage.getDestination()).willReturn(destination);
        given(destination.getBrokerName()).willReturn(null);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

    @Test
    public void testWhenNullDestination() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);

        given(failedMessage.getDestination()).willReturn(null);

        boolean result = underTest.test(failedMessage);

        assertThat(result, is(false));
    }

}