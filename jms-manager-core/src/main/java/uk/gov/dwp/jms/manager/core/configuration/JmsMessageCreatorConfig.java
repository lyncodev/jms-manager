package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.core.jms.send.FailedMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.SendMessageCreatorFactory;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.MessageDecorator;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.SendAttemptsDecorator;

import java.util.Collection;

import static java.util.Arrays.asList;

@Configuration
public class JmsMessageCreatorConfig {
    private static Collection<MessageDecorator> decorators = asList(
            new SendAttemptsDecorator()
    );

    @Bean
    public FailedMessageCreatorFactory failedMessageCreatorFactory () {
        return new FailedMessageCreatorFactory(decorators);
    }

    @Bean
    public SendMessageCreatorFactory sendMessageCreatorFactory () {
        return new SendMessageCreatorFactory(decorators);
    }
}
