package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource(value = "file:./app.properties", ignoreResourceNotFound = true)
})
public class PropertiesConfiguration {
}
