package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.jms.manager.core.util.HashMapBuilder;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.dwp.jms.manager.core.util.HashMapBuilder.newHashMap;

public class ActiveMQMessagePropertyExtractor implements MessagePropertyExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQMessagePropertyExtractor.class);

    @Override
    public Map<String, Object> extractProperties(Message in) {
        isMessageOfCorrectType(in);
        ActiveMQMessage message = (ActiveMQMessage)in;
        Map<String, Object> properties = new HashMap<>();
        try {
            extractOriginalDestination(properties, message.getOriginalDestination());
            extractTimestamp(properties, "timestamp", message.getTimestamp());
            extractTimestamp(properties, "brokerInTime", message.getBrokerInTime());
            extractTimestamp(properties, "brokerOutTime", message.getBrokerOutTime());
            for (Enumeration propertyNames = message.getPropertyNames(); propertyNames.hasMoreElements(); ) {
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

    public void isMessageOfCorrectType(Message in) {
        if (!(in instanceof ActiveMQMessage)) {
            String errorMessage;
            if (in == null) {
                errorMessage = "Message cannot be null");
            } else {
                errorMessage = in.getClass() + " is not an instance of ActiveMQMessage";
            }
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void extractOriginalDestination(Map<String, Object> properties, ActiveMQDestination originalDestination) {
        if (originalDestination != null) {
            properties.put("originalDestination", newHashMap()
                    .put("destinationType", originalDestination.getDestinationTypeAsString())
                    .put("name", originalDestination.getPhysicalName())
                    .build()
            );
        }
    }

    public void extractTimestamp(Map<String, Object> properties, String key, long brokerOutTime) {
        if (brokerOutTime != 0) {
            properties.put(key, new Date(brokerOutTime));
        }
    }
}
