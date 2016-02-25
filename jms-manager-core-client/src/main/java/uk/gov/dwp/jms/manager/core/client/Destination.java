package uk.gov.dwp.jms.manager.core.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Destination {

    private final String brokerName;
    private final String name;

    public Destination(@JsonProperty("brokerName") String brokerName,
                       @JsonProperty("name") String name) {
        this.brokerName = brokerName;
        this.name = name;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Destination that = (Destination) o;

        return brokerName.equals(that.brokerName)
                && ((name != null) ? name.equals(that.name) : (that.name == null));
    }

    @Override
    public int hashCode() {
        return 31 * brokerName.hashCode() + (name != null ? name.hashCode() : 0);
    }
}
