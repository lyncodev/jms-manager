package uk.gov.dwp.jms.manager.core.jms.replay;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.service.FailedMessageService;

import java.util.function.Function;

public class FailedMessageReplayer {

    private final JmsTemplate jmsTemplate;
    private final FailedMessageService failedMessageService;
    private final Function<FailedMessage, MessageCreator> failedMessageCreatorFactory;

    public FailedMessageReplayer(JmsTemplate jmsTemplate,
                                 FailedMessageService failedMessageService,
                                 Function<FailedMessage, MessageCreator> failedMessageCreatorFactory) {
        this.jmsTemplate = jmsTemplate;
        this.failedMessageService = failedMessageService;
        this.failedMessageCreatorFactory = failedMessageCreatorFactory;
    }

    public void replay(FailedMessage failedMessage, String queueName) {
        jmsTemplate.send(queueName, failedMessageCreatorFactory.apply(failedMessage));
        failedMessageService.remove(failedMessage.getFailedMessageId());
    }
}
