package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;
import uk.gov.dwp.jms.manager.core.jms.config.Brokers;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;

import java.util.HashMap;

@Configuration
public class JmsSenderConfig {
    @Autowired
    private Brokers brokers;

    @Bean
    public MessageSenderFactory messageSenderFactory () {
        HashMap<String, MessageSender> messageSenderMap = new HashMap<>();
        for (Broker broker : brokers.getBrokers()) {
            messageSenderMap.put(broker.getName(), new MessageSender(new JmsTemplate(broker.createConnectionFactory())));
        }
        return new MessageSenderFactory(messageSenderMap);
    }
}
