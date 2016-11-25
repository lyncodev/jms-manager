package uk.gov.dwp.jms.manager.core.jms.send;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Map;

public class SimpleMessageCreator implements MessageCreator {
    private final String content;
    private final Map<String, Object> properties;

    public SimpleMessageCreator(String content, Map<String, Object> properties) {
        this.content = content;
        this.properties = properties;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        TextMessage textMessage = session.createTextMessage(content);
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            textMessage.setObjectProperty(entry.getKey(), entry.getValue());
        }
        return textMessage;
    }
}
