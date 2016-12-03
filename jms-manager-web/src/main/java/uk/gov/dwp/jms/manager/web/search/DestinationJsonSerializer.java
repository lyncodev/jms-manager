package uk.gov.dwp.jms.manager.web.search;

import client.Destination;

public class DestinationJsonSerializer {

    public String asJson(Destination destination) {
        if (destination == null) {
            return null;
        } else {
            return new StringBuilder("{ ")
                    .append("broker: \"").append(destination.getBrokerName()).append("\", ")
                    .append("queue: \"").append(destination.getName()).append("\"")
                    .append("}")
                    .toString();
        }
    }
}
