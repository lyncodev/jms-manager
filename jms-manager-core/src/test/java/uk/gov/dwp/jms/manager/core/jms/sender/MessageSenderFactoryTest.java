package uk.gov.dwp.jms.manager.core.jms.sender;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class MessageSenderFactoryTest {
    private HashMap<String, MessageSender> messageSenderMap = new HashMap<>();
    private MessageSenderFactory underTest = new MessageSenderFactory(messageSenderMap);

    @Before
    public void setUp() throws Exception {
        messageSenderMap.clear();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void senderForNotFound() throws Exception {
        String brokerName = "unknown";

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("No sender for broker "+brokerName);

        underTest.senderFor(brokerName);
    }

    @Test
    public void senderForFound() throws Exception {
        String brokerName = "known";
        MessageSender messageSender = mock(MessageSender.class);

        messageSenderMap.put(brokerName, messageSender);

        MessageSender result = underTest.senderFor(brokerName);

        assertSame(messageSender, result);
    }

}