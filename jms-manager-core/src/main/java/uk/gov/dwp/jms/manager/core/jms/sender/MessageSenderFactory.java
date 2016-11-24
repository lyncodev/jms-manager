package uk.gov.dwp.jms.manager.core.jms.sender;

import java.util.Map;
import java.util.Optional;

public class MessageSenderFactory {
    private final Map<String, MessageSender> messageSenderMap;

    public MessageSenderFactory(Map<String, MessageSender> messageSenderMap) {
        this.messageSenderMap = messageSenderMap;
    }

    public MessageSender senderFor (String brokerName) {
        return Optional.ofNullable(messageSenderMap.get(brokerName))
                .orElseThrow(() -> new IllegalStateException("No sender for broker " + brokerName));
    }
}
