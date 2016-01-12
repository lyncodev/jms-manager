package uk.gov.dwp.jms.manager.core.jms;

import org.junit.Test;

import javax.jms.Message;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageWithPropertiesAdapterListenerTest {

    private final MessageTextExtractor messageTextExtractor = mock(MessageTextExtractor.class);
    private final ActiveMQMessagePropertyExtractor messagePropertyExtractor = mock(ActiveMQMessagePropertyExtractor.class);
    private final MessageWithPropertiesListener messageWithPropertiesListener = mock(MessageWithPropertiesListener.class);

    private final MessageWithPropertiesAdapterListener underTest = new MessageWithPropertiesAdapterListener(messageTextExtractor, messagePropertyExtractor, messageWithPropertiesListener);

    @Test
    public void adaptToMessageWithProperties() throws Exception {

        Message message = mock(Message.class);
        when(messageTextExtractor.extractText(message)).thenReturn("Text");
        when(messagePropertyExtractor.extractProperties(message)).thenReturn(new HashMap<>());

        underTest.onMessage(message);

        verify(messageWithPropertiesListener).onMessage(new MessageWithProperties("Text", new HashMap<>()));
    }
}