package uk.gov.dwp.jms.manager.core.classification.action.delay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import uk.gov.dwp.jms.manager.client.jackson.JacksonObjectMapperFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class AbsoluteDelayTest {
    ObjectMapper objectMapper = JacksonObjectMapperFactory.create();

    @Test
    public void serialise() throws Exception {
        ZonedDateTime dateTime = ZonedDateTime.of(2016, 10, 20, 2, 32, 23, 876, ZoneId.systemDefault());

        AbsoluteDelay input = new AbsoluteDelay(dateTime);
        String result = objectMapper.writeValueAsString(input);
        AbsoluteDelay parsed = objectMapper.readValue(result, AbsoluteDelay.class);

        assertEquals(input.triggerDate(), parsed.triggerDate().withZoneSameInstant(ZoneId.systemDefault()));
    }
}