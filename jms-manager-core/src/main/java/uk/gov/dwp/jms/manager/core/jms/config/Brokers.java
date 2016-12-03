package uk.gov.dwp.jms.manager.core.jms.config;

import java.util.List;

public class Brokers {
    private final List<Broker> brokers;

    public Brokers(List<Broker> brokers) {
        this.brokers = brokers;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }
}
