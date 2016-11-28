package uk.gov.dwp.jms.manager.core.jms.send;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.jms.send.decorator.MessageDecorator;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Collection;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleMessageCreatorTest {
    private final HashMap<String, Object> properties = new HashMap<>();
    private final String content = "content";
    private final MessageDecorator messageDecorator = mock(MessageDecorator.class);
    private Collection<MessageDecorator> messageDecorators = asList(messageDecorator);
    private final SimpleMessageCreator underTest = new SimpleMessageCreator(content, properties, messageDecorators);

    @Test
    public void create() throws Exception {
        Session session = mock(Session.class);
        TextMessage textMessage = mock(TextMessage.class);

        given(session.createTextMessage(content)).willReturn(textMessage);
        properties.put("JMSDeliveryMode", "PERSISTENT");

        Message result = underTest.createMessage(session);

        assertSame(textMessage, result);
        verify(textMessage).setObjectProperty("JMSDeliveryMode", "PERSISTENT");
        verify(messageDecorator).decorate(textMessage);
    }
}