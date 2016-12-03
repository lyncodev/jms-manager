package uk.gov.dwp.jms.manager.web.configuration;

import client.DestinationStatisticsResource;
import client.FailedMessageRemoveResource;
import client.FailedMessageResource;
import client.FailedMessageSearchResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.web.search.FailedMessageListController;
import uk.gov.dwp.jms.manager.web.search.FailedMessagesJsonSerializer;
import uk.gov.dwp.jms.manager.web.summary.DestinationStatisticsController;
import uk.gov.dwp.jms.manager.web.summary.DestinationStatisticsJsonSerializer;

@Configuration
@Import({
        JacksonConfiguration.class,
        JmsManagerCoreClientConfiguration.class
})
public class ControllerConfiguration {

    @Bean
    public FailedMessageListController failedMessageListController(FailedMessageResource failedMessageResource,
                                                                   FailedMessageSearchResource failedMessageSearchResource,
                                                                   FailedMessageRemoveResource failedMessageRemoveResource) {
        return new FailedMessageListController(failedMessageResource, failedMessageSearchResource, failedMessageRemoveResource, new FailedMessagesJsonSerializer());
    }

    @Bean
    public DestinationStatisticsController destinationStatisticsController(DestinationStatisticsResource destinationStatisticsResource,
                                                                           ObjectMapper objectMapper) {
        return new DestinationStatisticsController(destinationStatisticsResource, new DestinationStatisticsJsonSerializer(objectMapper));
    }
}
