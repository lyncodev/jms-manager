package uk.gov.dwp.jms.manager.core.jms.send;

import java.util.Map;
import java.util.Optional;

public class MessageSenderFactory {
    private final Map<String, MessageSender> messageSenderMap;

    public MessageSenderFactory(Map<String, MessageSender> messageSenderMap) {
        this.messageSenderMap = messageSenderMap;
    }

    public MessageSender senderFor (String brokerName) {
        return Optional.ofNullable(messageSenderMap.get(brokerName))
                .orElseThrow(() -> new IllegalStateException("No send for broker " + brokerName));
    }
}
