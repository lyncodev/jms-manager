package uk.gov.dwp.jms.manager;

import org.apache.commons.lang3.builder.Builder;
import uk.gov.dwp.jms.manager.broker.BrokerServiceSetup;
import uk.gov.dwp.jms.manager.core.classification.MessageClassifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IntegrationSetupBuilder implements Builder<IntegrationSetup> {
    public static IntegrationSetupBuilder integrationSetup () {
        return new IntegrationSetupBuilder();
    }

    private List<String> brokers = new ArrayList<>();
    private List<MessageClassifier> classifiers = new ArrayList<>();

    public IntegrationSetupBuilder withBroker (String brokerName) {
        brokers.add(brokerName);
        return this;
    }

    public IntegrationSetupBuilder withMessageClassifier (MessageClassifier messageClassifier) {
        classifiers.add(messageClassifier);
        return this;
    }

    @Override
    public IntegrationSetup build() {
        List<BrokerServiceSetup> brokerStarters = brokers.stream().map(BrokerServiceSetup::new).collect(Collectors.toList());
        return new IntegrationSetup(
                new JmsManagerCoreSetup(new JmsManagerCoreConfiguration(
                        brokerStarters.stream()
                                .map(BrokerServiceSetup::broker)
                                .collect(Collectors.toList()),
                        classifiers,
                        "mongodb://admin:Passw0rd@localhost:27017/admin",
                        UUID.randomUUID().toString())),
                brokerStarters.stream().collect(Collectors.toMap(
                        x -> x.broker().getName(),
                        Function.identity()
                ))
        );
    }
}
