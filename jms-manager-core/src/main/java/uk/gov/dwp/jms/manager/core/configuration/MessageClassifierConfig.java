package uk.gov.dwp.jms.manager.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.jms.manager.core.classification.MessageClassifier;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
public class MessageClassifierConfig {
    private static final Logger logger = LoggerFactory.getLogger(MessageClassifierConfig.class);

    @Value("${classifiers.location}")
    private String classifiersLocation;

    @Bean
    public FailedMessageClassifierProcessor messageClassifiers (ObjectMapper objectMapper, ApplicationContext applicationContext) throws IOException {
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, MessageClassifier.class);
        List<MessageClassifier> messageClassifiers = objectMapper.readValue(new File(classifiersLocation), collectionType);

        logger.info("Loaded {} classifiers", messageClassifiers.size());

        return new FailedMessageClassifierProcessor(messageClassifiers, applicationContext);
    }
}
