package uk.gov.dwp.jms.manager.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dwp.jms.manager.core.client.Id;

import java.io.IOException;

public class IdDeserializer extends JsonDeserializer<Id> {
    @Override
    public Id deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
