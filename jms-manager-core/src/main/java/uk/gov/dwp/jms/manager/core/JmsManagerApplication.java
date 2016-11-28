package uk.gov.dwp.jms.manager.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:./app.properties")
public class JmsManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmsManagerApplication.class, args);
    }
}
