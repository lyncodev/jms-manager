package uk.gov.dwp.jms.manager.core.jms;

import uk.gov.dwp.jms.manager.core.client.Destination;

import javax.jms.Message;

public interface DestinationExtractor<T extends Message>  {

    Destination extractDestination(T message);
}
