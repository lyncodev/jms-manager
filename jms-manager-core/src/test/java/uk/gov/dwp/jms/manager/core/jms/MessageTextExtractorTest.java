package uk.gov.dwp.jms.manager.core.jms;

import org.apache.activemq.command.ActiveMQMessage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.jms.BytesMessage;
import javax.jms.TextMessage;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageTextExtractorTest {

    private final MessageTextExtractor underTest = new MessageTextExtractor();

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void extractTextFromMessage() throws Exception {
        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn("Text");
        assertThat(underTest.extractText(textMessage), is("Text"));
    }

    @Test
    public void throwsExceptionIfNotTextMessage() throws Exception {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Expected TextMessage received: org.apache.activemq.command.ActiveMQMessage");

        underTest.extractText(new ActiveMQMessage());
    }
}