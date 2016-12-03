package uk.gov.dwp.jms.manager.core.jms.activemq;

import client.Destination;
import client.FailedMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.gov.dwp.jms.manager.core.jms.DestinationExtractor;
import uk.gov.dwp.jms.manager.core.jms.MessagePropertyExtractor;
import uk.gov.dwp.jms.manager.core.jms.MessageTextExtractor;

import javax.jms.JMSException;
import javax.jms.Message;
import java.time.ZonedDateTime;
import java.util.Enumeration;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.jms.manager.core.domain.DestinationMatcher.aDestination;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;

public class ActiveMQFailedMessageFactoryTest {

    private static final ZonedDateTime NOW = now(UTC);

    private final MessageTextExtractor messageTextExtractor = mock(MessageTextExtractor.class);
    private final DestinationExtractor destinationExtractor = mock(DestinationExtractor.class);
    private final MessagePropertyExtractor messagePropertyExtractor = mock(MessagePropertyExtractor.class);

    private final ActiveMQFailedMessageFactory underTest = new ActiveMQFailedMessageFactory(messageTextExtractor, destinationExtractor, messagePropertyExtractor);
    private final ActiveMQMessage message = mock(ActiveMQMessage.class);

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void exceptionIsThrownIfMessageIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Message cannot be null");
        underTest.createFailedMessage(null);
    }

    @Test
    public void exceptionIsThrownIfMessageIsNotActiveMQMessage() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Expected ActiveMQMessage received: uk.gov.dwp.jms.manager.core.jms.activemq.ActiveMQFailedMessageFactoryTest$SomeMessage");

        underTest.createFailedMessage(new SomeMessage());
    }


    @Test
    public void createAFailedMessage() throws Exception {

        when(messageTextExtractor.extractText(message)).thenReturn("Some text");
        when(destinationExtractor.extractDestination(message)).thenReturn(new Destination("broker.name", "queue.name"));
        when(messagePropertyExtractor.extractProperties(message)).thenReturn(emptyMap());
        when(message.getTimestamp()).thenReturn(NOW.minusSeconds(5).toInstant().toEpochMilli());
        when(message.getBrokerInTime()).thenReturn(NOW.toInstant().toEpochMilli());

        FailedMessage failedMessage = underTest.createFailedMessage(message);

        assertThat(failedMessage, is(aFailedMessage()
                .withContent(equalTo("Some text"))
                .withDestination(aDestination().withBrokerName("broker.name").withName("queue.name"))
                .withSentAt(equalTo(NOW.minusSeconds(5)))
                .withFailedAt(equalTo(NOW))
                .withProperties(equalTo(emptyMap()))));
    }

    static class SomeMessage implements Message {

        @Override
        public String getJMSMessageID() throws JMSException {
            return null;
        }

        @Override
        public void setJMSMessageID(String id) throws JMSException {

        }

        @Override
        public long getJMSTimestamp() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSTimestamp(long timestamp) throws JMSException {

        }

        @Override
        public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
            return new byte[0];
        }

        @Override
        public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {

        }

        @Override
        public void setJMSCorrelationID(String correlationID) throws JMSException {

        }

        @Override
        public String getJMSCorrelationID() throws JMSException {
            return null;
        }

        @Override
        public javax.jms.Destination getJMSReplyTo() throws JMSException {
            return null;
        }

        @Override
        public void setJMSReplyTo(javax.jms.Destination replyTo) throws JMSException {

        }

        @Override
        public javax.jms.Destination getJMSDestination() throws JMSException {
            return null;
        }

        @Override
        public void setJMSDestination(javax.jms.Destination destination) throws JMSException {

        }

        @Override
        public int getJMSDeliveryMode() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSDeliveryMode(int deliveryMode) throws JMSException {

        }

        @Override
        public boolean getJMSRedelivered() throws JMSException {
            return false;
        }

        @Override
        public void setJMSRedelivered(boolean redelivered) throws JMSException {

        }

        @Override
        public String getJMSType() throws JMSException {
            return null;
        }

        @Override
        public void setJMSType(String type) throws JMSException {

        }

        @Override
        public long getJMSExpiration() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSExpiration(long expiration) throws JMSException {

        }

        @Override
        public int getJMSPriority() throws JMSException {
            return 0;
        }

        @Override
        public void setJMSPriority(int priority) throws JMSException {

        }

        @Override
        public void clearProperties() throws JMSException {

        }

        @Override
        public boolean propertyExists(String name) throws JMSException {
            return false;
        }

        @Override
        public boolean getBooleanProperty(String name) throws JMSException {
            return false;
        }

        @Override
        public byte getByteProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public short getShortProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public int getIntProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public long getLongProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public float getFloatProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public double getDoubleProperty(String name) throws JMSException {
            return 0;
        }

        @Override
        public String getStringProperty(String name) throws JMSException {
            return null;
        }

        @Override
        public Object getObjectProperty(String name) throws JMSException {
            return null;
        }

        @Override
        public Enumeration getPropertyNames() throws JMSException {
            return null;
        }

        @Override
        public void setBooleanProperty(String name, boolean value) throws JMSException {

        }

        @Override
        public void setByteProperty(String name, byte value) throws JMSException {

        }

        @Override
        public void setShortProperty(String name, short value) throws JMSException {

        }

        @Override
        public void setIntProperty(String name, int value) throws JMSException {

        }

        @Override
        public void setLongProperty(String name, long value) throws JMSException {

        }

        @Override
        public void setFloatProperty(String name, float value) throws JMSException {

        }

        @Override
        public void setDoubleProperty(String name, double value) throws JMSException {

        }

        @Override
        public void setStringProperty(String name, String value) throws JMSException {

        }

        @Override
        public void setObjectProperty(String name, Object value) throws JMSException {

        }

        @Override
        public void acknowledge() throws JMSException {

        }

        @Override
        public void clearBody() throws JMSException {

        }
    }
}