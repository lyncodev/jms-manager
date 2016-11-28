package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JmsMessagePropertyExtractor implements MessagePropertyExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsMessagePropertyExtractor.class);

    @Override
    public Map<String, Object> extractProperties(ActiveMQMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        Map<String, Object> properties = new HashMap<>();
        try {
            for (Enumeration propertyNames = message.getAllPropertyNames(); propertyNames.hasMoreElements(); ) {
                String propertyName = (String) propertyNames.nextElement();
                try {
                    properties.put(propertyName, message.getObjectProperty(propertyName));
                } catch (JMSException e) {
                    String errorMessage = String.format("Could not extract property: '%s' from Message '%s'", propertyName, message);
                    LOGGER.error(errorMessage, e);
                    throw new RuntimeException(errorMessage, e);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
