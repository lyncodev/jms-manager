package uk.gov.dwp.jms.manager.core.classification.action.delay;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import uk.gov.dwp.jms.manager.core.service.jackson.ClasspathTypeIdResolver;

import java.time.ZonedDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(Delay.TypeIdResolver.class)
public interface Delay {
    ZonedDateTime triggerDate ();

    class TypeIdResolver extends ClasspathTypeIdResolver {
        @Override
        protected Class baseType() {
            return Delay.class;
        }
    }
}
