package uk.gov.dwp.jms.manager.client.jackson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.gov.dwp.jms.manager.client.Id;

public class JacksonObjectMapperFactory {
    public static ObjectMapper create () {
        return new ObjectMapper()
                .setDateFormat(new ISO8601DateFormatWithMilliSeconds())
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new SimpleModule()
                        .addSerializer(Id.class, new IdSerializer())
                        .addDeserializer(Id.class, new IdDeserializer()));
    }
}
