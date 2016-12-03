package uk.gov.dwp.jms.manager;

import org.apache.commons.io.FileUtils;
import uk.gov.dwp.jms.manager.broker.BrokerManager;
import uk.gov.dwp.jms.manager.broker.BrokerServiceSetup;

import java.io.File;
import java.util.Map;

public class IntegrationSetup {
    private final JmsManagerCoreSetup jmsManagerCoreSetup;
    private final Map<String, BrokerServiceSetup> brokerServiceSetups;

    public IntegrationSetup(JmsManagerCoreSetup jmsManagerCoreSetup, Map<String, BrokerServiceSetup> brokerServiceSetups) {
        this.jmsManagerCoreSetup = jmsManagerCoreSetup;
        this.brokerServiceSetups = brokerServiceSetups;
    }

    public IntegrationSetup start () throws Exception {
        FileUtils.deleteDirectory(new File(BrokerServiceSetup.DATA_DIRECTORY));

        for (BrokerServiceSetup brokerServiceSetup : brokerServiceSetups.values()) {
            brokerServiceSetup.start();
        }

        jmsManagerCoreSetup.start();

        return this;
    }

    public void stop () throws Exception {
        jmsManagerCoreSetup.stop();

        for (BrokerServiceSetup brokerServiceSetup : brokerServiceSetups.values()) {
            brokerServiceSetup.stop();
        }
    }

    public BrokerManager broker(String name) {
        return new BrokerManager(brokerServiceSetups.get(name));
    }

    public <T> T getBean(Class<T> type) {
        return jmsManagerCoreSetup.getBean(type);
    }
}
