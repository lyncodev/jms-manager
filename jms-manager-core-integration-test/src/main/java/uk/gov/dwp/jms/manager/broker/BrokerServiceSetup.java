package uk.gov.dwp.jms.manager.broker;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.Destination;
import org.apache.activemq.command.ActiveMQDestination;
import uk.gov.dwp.jms.manager.core.jms.config.Broker;

import java.util.HashMap;
import java.util.Map;

public class BrokerServiceSetup {
    public static final String DATA_DIRECTORY = "activemq-data";

    private final String name;
    private BrokerService brokerService;

    public BrokerServiceSetup(String name) {
        this.name = name;
    }

    public void start () throws Exception {
        brokerService = new BrokerService();
        brokerService.setDataDirectory(DATA_DIRECTORY);
        brokerService.setBrokerName(name);
        brokerService.start();

    }

    public Broker broker () {
        return new Broker(
                name,
                String.format("vm://%s", name),
                "admin", "password"
        );
    }

    public Map<String, Long> messagesOnQueues () {
        Map<String, Long> count = new HashMap<>();
        Map<ActiveMQDestination, Destination> destinationMap = brokerService.getRegionBroker().getDestinationMap();
        for (Map.Entry<ActiveMQDestination, Destination> entry : destinationMap.entrySet()) {
            if (entry.getKey().isQueue()) {
                count.put(entry.getValue().getName(), entry.getValue().getDestinationStatistics().getMessages().getCount());
            }
        }
        return count;
    }

    public void stop () throws Exception {
        brokerService.stop();
    }
}
