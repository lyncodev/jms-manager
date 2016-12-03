package uk.gov.dwp.jms.manager.core.classification.action;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.service.jackson.ClasspathTypeIdResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(FailedMessageAction.TypeIdResolver.class)
public interface FailedMessageAction {
    void perform (Request request);

    class Request {
        private final FailedMessage failedMessage;
        private final ApplicationContext applicationContext;

        public Request(FailedMessage failedMessage, ApplicationContext applicationContext) {
            this.failedMessage = failedMessage;
            this.applicationContext = applicationContext;
        }

        public FailedMessage getFailedMessage() {
            return failedMessage;
        }

        public ApplicationContext getApplicationContext() {
            return applicationContext;
        }
    }

    class TypeIdResolver extends ClasspathTypeIdResolver {
        @Override
        protected Class baseType() {
            return FailedMessageAction.class;
        }
    }
}
