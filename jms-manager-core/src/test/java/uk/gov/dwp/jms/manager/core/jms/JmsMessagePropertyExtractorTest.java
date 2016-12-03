package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.jms.JMSException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyEnumeration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JmsMessagePropertyExtractorTest {

    @Rule
    public ExpectedException expectedException = none();

    private final JmsMessagePropertyExtractor underTest = new JmsMessagePropertyExtractor();
    private final ActiveMQMessage message = mock(ActiveMQMessage.class);

    @Test
    public void exceptionIsThrownIfMessageIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Message cannot be null");
        underTest.extractProperties(null);
    }

    @Test
    public void messageWithNoProperties() throws JMSException {
        when(message.getAllPropertyNames()).thenReturn(emptyEnumeration());
        Map<String, Object> properties = underTest.extractProperties(message);
        assertThat(properties, equalTo(Collections.emptyMap()));
    }

    @Test
    public void messageWithProperties() throws Exception {
        Enumeration<String> messageProperties = messageProperties(Pair.of("foo", "bar"), Pair.of("one", 1));
        when(message.getAllPropertyNames()).thenReturn(messageProperties);

        Map<String, Object> actual = underTest.extractProperties(message);
        assertThat(actual, hasEntry("foo", "bar"));
        assertThat(actual, hasEntry("one", 1));
    }

    private Enumeration<String> messageProperties(Pair<String, Object>...names) {
        Vector<String> propertyNames = new Vector<>();
        asList(names).forEach(pair -> {
            try {
                when(message.getObjectProperty(pair.getKey())).thenReturn(pair.getValue());
                propertyNames.add(pair.getKey());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
        return propertyNames.elements();
    }
}