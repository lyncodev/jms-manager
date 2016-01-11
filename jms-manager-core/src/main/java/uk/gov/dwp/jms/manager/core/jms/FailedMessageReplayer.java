package uk.gov.dwp.jms.manager.core.jms;

import org.springframework.jms.core.JmsTemplate;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Map;

public class FailedMessageReplayer {

    private final JmsTemplate jmsTemplate;
    private final FailedMessageService failedMessageService;

    public FailedMessageReplayer(JmsTemplate jmsTemplate, FailedMessageService failedMessageService) {
        this.jmsTemplate = jmsTemplate;
        this.failedMessageService = failedMessageService;
    }

    public void replay(FailedMessage failedMessage, String queueName) {
        jmsTemplate.send(queueName, session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(failedMessage.getContent());
            Map<String, Object> properties = failedMessage.getProperties();
            properties.keySet().forEach(key -> {
                try {
                    textMessage.setObjectProperty(key, properties.get(key));
                } catch (JMSException ignore) {
                    // TODO: Should we just ignore the fact we couldn't set a property on the message?
                }
            });
            return textMessage;
        });
        failedMessageService.remove(failedMessage.getFailedMessageId());
    }
}
