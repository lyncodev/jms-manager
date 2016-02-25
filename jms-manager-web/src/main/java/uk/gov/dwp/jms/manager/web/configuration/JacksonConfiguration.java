package uk.gov.dwp.jms.manager.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JacksonJsonProvider jacksonJsonProvider() {
        return new JacksonJsonProvider(objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
//                .setDateFormat(new ISO8601DateFormatWithMilliSeconds())
                .registerModule(new JavaTimeModule())
//                .registerModule(new SimpleModule()
//                        .addSerializer(Id.class, new IdSerializer())
//                        .addDeserializer(Id.class, new IdDeserializer()));
                ;
    }
}
