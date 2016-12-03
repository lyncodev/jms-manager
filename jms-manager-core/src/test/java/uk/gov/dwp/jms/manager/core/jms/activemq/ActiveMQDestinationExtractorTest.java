package uk.gov.dwp.jms.manager.core.jms.activemq;

import uk.gov.dwp.jms.manager.client.Destination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActiveMQDestinationExtractorTest {
    
    private final ActiveMQDestinationExtractor underTest = new ActiveMQDestinationExtractor("internal");

    @Test
    public void createDestination() throws Exception {
        ActiveMQMessage message = mock(ActiveMQMessage.class);
        when(message.getOriginalDestination()).thenReturn(new ActiveMQQueue("queue.name"));
        Destination destination = underTest.extractDestination(message);
        assertThat(destination, equalTo(new Destination("internal", "queue.name")));

    }

    @Test
    public void messageSentViaActiveMQAdminInterface() throws Exception {
        ActiveMQMessage activeMQMessage = mock(ActiveMQMessage.class);

        when(activeMQMessage.getOriginalDestination()).thenReturn(null);

        Destination result = underTest.extractDestination(activeMQMessage);

        assertThat(result.getBrokerName(), is("internal"));
        assertThat(result.getName(), nullValue());
    }
}