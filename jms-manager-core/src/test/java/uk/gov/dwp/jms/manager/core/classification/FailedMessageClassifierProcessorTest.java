package uk.gov.dwp.jms.manager.core.classification;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.predicate.FailedMessagePredicate;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageClassifierProcessor;

import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class FailedMessageClassifierProcessorTest {
    private final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private final ArrayList<MessageClassifier> messageClassifiers = new ArrayList<>();
    private FailedMessageClassifierProcessor underTest = new FailedMessageClassifierProcessor(messageClassifiers, applicationContext);

    @Before
    public void setUp() throws Exception {
        messageClassifiers.clear();
    }

    @Test
    public void onMessageNoInterruption() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageClassifier messageClassifier1 = mock(MessageClassifier.class);
        MessageClassifier messageClassifier2 = mock(MessageClassifier.class);
        FailedMessagePredicate failedMessagePredicate1 = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction1 = mock(FailedMessageAction.class);
        FailedMessagePredicate failedMessagePredicate2 = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction2 = mock(FailedMessageAction.class);

        messageClassifiers.add(messageClassifier1);
        messageClassifiers.add(messageClassifier2);
        given(messageClassifier1.continueChain()).willReturn(true);
        given(messageClassifier1.getPredicate()).willReturn(failedMessagePredicate1);
        given(messageClassifier1.getAction()).willReturn(failedMessageAction1);
        given(failedMessagePredicate1.test(failedMessage)).willReturn(true);
        given(messageClassifier2.continueChain()).willReturn(true);
        given(messageClassifier2.getPredicate()).willReturn(failedMessagePredicate2);
        given(messageClassifier2.getAction()).willReturn(failedMessageAction2);
        given(failedMessagePredicate2.test(failedMessage)).willReturn(true);

        underTest.process(failedMessage);

        verify(failedMessageAction1).perform(argThat(requestWithMessage(equalTo(failedMessage))));
        verify(failedMessageAction2).perform(argThat(requestWithMessage(equalTo(failedMessage))));
    }

    @Test
    public void onMessageWithInterruption() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageClassifier messageClassifier1 = mock(MessageClassifier.class);
        MessageClassifier messageClassifier2 = mock(MessageClassifier.class);
        FailedMessagePredicate failedMessagePredicate1 = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction1 = mock(FailedMessageAction.class);
        FailedMessagePredicate failedMessagePredicate2 = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction2 = mock(FailedMessageAction.class);

        messageClassifiers.add(messageClassifier1);
        messageClassifiers.add(messageClassifier2);
        given(messageClassifier1.continueChain()).willReturn(false);
        given(messageClassifier1.getPredicate()).willReturn(failedMessagePredicate1);
        given(messageClassifier1.getAction()).willReturn(failedMessageAction1);
        given(failedMessagePredicate1.test(failedMessage)).willReturn(true);
        given(messageClassifier2.continueChain()).willReturn(false);
        given(messageClassifier2.getPredicate()).willReturn(failedMessagePredicate2);
        given(messageClassifier2.getAction()).willReturn(failedMessageAction2);
        given(failedMessagePredicate2.test(failedMessage)).willReturn(true);

        underTest.process(failedMessage);

        verify(failedMessageAction1).perform(argThat(requestWithMessage(equalTo(failedMessage))));
        verify(failedMessageAction2, never()).perform(any());
    }

    @Test
    public void onMessagePredicateWorks() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageClassifier messageClassifier = mock(MessageClassifier.class);
        FailedMessagePredicate failedMessagePredicate = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction = mock(FailedMessageAction.class);

        messageClassifiers.add(messageClassifier);
        given(messageClassifier.continueChain()).willReturn(false);
        given(messageClassifier.getPredicate()).willReturn(failedMessagePredicate);
        given(messageClassifier.getAction()).willReturn(failedMessageAction);
        given(failedMessagePredicate.test(failedMessage)).willReturn(true);

        underTest.process(failedMessage);

        verify(failedMessageAction).perform(argThat(requestWithMessage(equalTo(failedMessage))));
    }

    @Test
    public void onMessagePredicateFails() throws Exception {
        FailedMessage failedMessage = mock(FailedMessage.class);
        MessageClassifier messageClassifier = mock(MessageClassifier.class);
        FailedMessagePredicate failedMessagePredicate = mock(FailedMessagePredicate.class);
        FailedMessageAction failedMessageAction = mock(FailedMessageAction.class);

        messageClassifiers.add(messageClassifier);
        given(messageClassifier.continueChain()).willReturn(false);
        given(messageClassifier.getPredicate()).willReturn(failedMessagePredicate);
        given(messageClassifier.getAction()).willReturn(failedMessageAction);
        given(failedMessagePredicate.test(failedMessage)).willReturn(false);

        underTest.process(failedMessage);

        verify(failedMessageAction, never()).perform(any());
    }

    private Matcher<FailedMessageAction.Request> requestWithMessage (Matcher<FailedMessage> failedMessageMatcher) {
        return new FeatureMatcher<FailedMessageAction.Request, FailedMessage>(failedMessageMatcher, "failedMessage", "failedMessage") {
            @Override
            protected FailedMessage featureValueOf(FailedMessageAction.Request actual) {
                return actual.getFailedMessage();
            }
        };
    }
}