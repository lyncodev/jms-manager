package uk.gov.dwp.jms.manager.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "file:./app.properties", ignoreResourceNotFound = true)
})
public class JmsManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsManagerApplication.class, args);
    }
}
