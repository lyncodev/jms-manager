package uk.gov.dwp.jms.manager.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.client.jackson.JacksonObjectMapperFactory;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JacksonJsonProvider jacksonJsonProvider(ObjectMapper objectMapper) {
        return new JacksonJsonProvider(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonObjectMapperFactory.create();
    }
}
