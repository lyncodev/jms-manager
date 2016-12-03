package uk.gov.dwp.jms.manager.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;
import uk.gov.dwp.jms.manager.core.jms.config.Brokers;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class BrokerConfig {
    @Value("${brokers.location}")
    private String brokersLocation;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Brokers brokers () throws IOException {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, Broker.class);
        List<Broker> brokers = objectMapper.readValue(new File(brokersLocation), collectionType);

        return new Brokers(brokers);
    }
}
