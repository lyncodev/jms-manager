package uk.gov.dwp.jms.manager.web.configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

import static java.util.Collections.singletonList;

@Configuration
@Import(JacksonConfiguration.class)
public class JmsManagerCoreClientConfiguration {

    @Bean
    public FailedMessageResource failedMessageResource(JacksonJsonProvider jacksonJsonProvider, ClientProperties clientProperties) {
        return JAXRSClientFactory.create(clientProperties.getCore().getUrl(), FailedMessageResource.class, singletonList(jacksonJsonProvider));
    }
}
