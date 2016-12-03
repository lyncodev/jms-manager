package uk.gov.dwp.jms.manager.core.classification.predicate;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import uk.gov.dwp.jms.manager.core.service.jackson.ClasspathTypeIdResolver;

import java.util.function.Predicate;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(FailedMessagePredicate.TypeIdResolver.class)
public interface FailedMessagePredicate extends Predicate<FailedMessage> {
    class TypeIdResolver extends ClasspathTypeIdResolver {
        @Override
        protected Class baseType() {
            return FailedMessagePredicate.class;
        }
    }
}
