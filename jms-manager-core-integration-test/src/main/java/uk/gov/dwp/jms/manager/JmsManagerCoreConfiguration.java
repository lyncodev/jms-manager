package uk.gov.dwp.jms.manager;

import uk.gov.dwp.jms.manager.core.classification.MessageClassifier;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;

import java.util.List;

public class JmsManagerCoreConfiguration {
    private final List<Broker> brokers;
    private final List<MessageClassifier> classifiers;
    private final String mongoClientUri;
    private final String databaseName;

    public JmsManagerCoreConfiguration(List<Broker> brokers, List<MessageClassifier> classifiers, String mongoClientUri, String databaseName) {
        this.brokers = brokers;
        this.classifiers = classifiers;
        this.mongoClientUri = mongoClientUri;
        this.databaseName = databaseName;
    }

    public List<MessageClassifier> getClassifiers() {
        return classifiers;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }

    public String getMongoClientUri() {
        return mongoClientUri;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
