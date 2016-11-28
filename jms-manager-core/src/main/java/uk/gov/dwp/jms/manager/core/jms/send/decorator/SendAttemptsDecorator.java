package uk.gov.dwp.jms.manager.core.jms.send.decorator;

import javax.jms.JMSException;
import javax.jms.Message;

public class SendAttemptsDecorator implements MessageDecorator {
    private static final String PROPERTY = "jms.manager.attempts";

    @Override
    public void decorate(Message message) {
        int currentCounter = getCurrentCounter(message);
        try {
            message.setIntProperty(PROPERTY, currentCounter + 1);
        } catch (JMSException e) {
            throw new RuntimeException(String.format("Unable to set property %s = %d", PROPERTY, currentCounter + 1));
        }
    }

    private int getCurrentCounter(Message message) {
        try {
            return message.getIntProperty(PROPERTY);
        } catch (Exception e) {
            return 0;
        }
    }
}
