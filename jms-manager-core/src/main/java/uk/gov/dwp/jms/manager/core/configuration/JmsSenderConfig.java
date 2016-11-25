package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import uk.gov.dwp.jms.manager.core.jms.ConnectionFactoryFactory;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSender;
import uk.gov.dwp.jms.manager.core.jms.send.MessageSenderFactory;

import java.util.HashMap;

@Configuration
public class JmsSenderConfig {
    @Autowired
    private Environment environment;

    @Bean
    public MessageSenderFactory messageSenderFactory () {
        ConnectionFactoryFactory connectionFactoryFactory = new ConnectionFactoryFactory(environment);
        String[] brokers = environment.getProperty("brokers", String[].class, new String[]{"default"});
        HashMap<String, MessageSender> messageSenderMap = new HashMap<>();
        for (String broker : brokers) {
            messageSenderMap.put(broker, new MessageSender(new JmsTemplate(connectionFactoryFactory.create(broker))));
        }
        return new MessageSenderFactory(messageSenderMap);
    }
}
