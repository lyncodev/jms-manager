package uk.gov.dwp.jms.manager.core.jms.sender.replay;

import org.junit.Before;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageBuilder.aFailedMessage;

public class FailedMessageCreatorTest {

    private final Session session = mock(Session.class);
    private final TextMessage textMessage = mock(TextMessage.class);

    private FailedMessageCreator underTest;

    @Before
    public void setUp() throws JMSException {
        when(session.createTextMessage()).thenReturn(textMessage);
    }

    @Test
    public void createAMessageWithNoProperties() throws Exception {
        underTest = new FailedMessageCreator(aFailedMessage().withContent("Hello").withProperties(emptyMap()).build());

        assertThat(underTest.createMessage(session), is(textMessage));

        verify(textMessage).setText("Hello");
        verifyNoMoreInteractions(textMessage);
    }

    @Test
    public void createAJmsMessageWithProperties() throws Exception {
        FailedMessage failedMessage = aFailedMessage()
                .withContent("Hello")
                .withProperty("name", "Bob")
                .withProperty("count", 3)
                .build();
        underTest = new FailedMessageCreator(failedMessage);

        assertThat(underTest.createMessage(session), is(textMessage));

        verify(textMessage).setText("Hello");
        verify(textMessage).setObjectProperty("name", "Bob");
        verify(textMessage).setObjectProperty("count", 3);
        verifyNoMoreInteractions(textMessage);
    }
}