package uk.gov.dwp.jms.manager.core.jms.activemq;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.Destination;

import static org.hamcrest.Matchers.equalTo;
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
}