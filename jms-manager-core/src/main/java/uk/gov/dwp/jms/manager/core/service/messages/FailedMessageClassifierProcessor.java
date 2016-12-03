package uk.gov.dwp.jms.manager.core.service.messages;

import client.FailedMessage;
import org.springframework.context.ApplicationContext;
import uk.gov.dwp.jms.manager.core.classification.MessageClassifier;
import uk.gov.dwp.jms.manager.core.classification.action.FailedMessageAction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class FailedMessageClassifierProcessor {
    private final List<MessageClassifier> messageClassifiers;
    private final ApplicationContext applicationContext;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    public FailedMessageClassifierProcessor(List<MessageClassifier> messageClassifiers, ApplicationContext applicationContext) {
        this.messageClassifiers = messageClassifiers;
        this.applicationContext = applicationContext;
    }

    public void process(FailedMessage message) {
        reentrantLock.lock();
        processMessage(message);
        reentrantLock.unlock();
    }

    public void process (Supplier<Collection<FailedMessage>> recordSupplier) {
        reentrantLock.lock();
        recordSupplier.get().forEach(this::processMessage);
        reentrantLock.unlock();
    }

    public void performAction (Supplier<Optional<FailedMessage>> messageId, FailedMessageAction action) {
        reentrantLock.lock();
        Optional<FailedMessage> failedMessage = messageId.get();
        if (failedMessage.isPresent()) {
            action.perform(new FailedMessageAction.Request(failedMessage.get(), applicationContext));
        }
        reentrantLock.unlock();
    }

    private void processMessage(FailedMessage message) {
        FailedMessageAction.Request request = new FailedMessageAction.Request(
                message, applicationContext
        );
        for (MessageClassifier messageClassifier : messageClassifiers) {
            if (messageClassifier.getPredicate().test(message)) {
                messageClassifier.getAction().perform(request);

                if (!messageClassifier.continueChain()) break;
            }
        }
    }
}
