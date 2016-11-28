package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.command.ActiveMQMessage;

import java.util.Map;

public interface MessagePropertyExtractor {

    Map<String, Object> extractProperties(ActiveMQMessage message);
}
