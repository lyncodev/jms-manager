package uk.gov.dwp.jms.manager.core.jms;

public interface MessageWithPropertiesListener {

    void onMessage(MessageWithProperties message);
}
