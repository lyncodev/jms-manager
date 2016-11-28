package uk.gov.dwp.jms.manager.core.jms.send.decorator;

import javax.jms.Message;

public interface MessageDecorator {
    void decorate (Message message);
}
