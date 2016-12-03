package uk.gov.dwp.jms.manager.core.dao.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "mongo-test.properties")
})
public class MongoTestProperties {

}
