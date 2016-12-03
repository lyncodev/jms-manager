package uk.gov.dwp.jms.manager;

import client.FailedMessage;
import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.classification.action.LabelFailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.predicate.BrokerPredicate;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

import java.util.SortedSet;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.classification.MessageClassifierBuilder.messageClassifierBuilder;
import static uk.gov.dwp.jms.manager.core.client.DestinationMatcherBuilder.destination;

public class LabelIntegrationTest {
    private IntegrationSetup setup;

    @After
    public void tearDown() throws Exception {
        setup.stop();
    }

    @Test
    public void labelTest() throws Exception {
        // given
        String content = "message";
        String queueName = "queue";
        String brokerName = "internal";
        String label = "PROBLEM";

        setup = IntegrationSetupBuilder.integrationSetup()
                .withBroker(brokerName)
                .withMessageClassifier(messageClassifierBuilder()
                        .withPredicate(new BrokerPredicate(brokerName))
                        .withAction(new LabelFailedMessageAction(label))
                        .build())
                .build().start();

        // when
        setup.broker(brokerName).sendAndDeadLetterMessage(queueName, content, RuntimeException::new);
        waitToProcessMessage(setup);

        // then
        FailedMessage failedMessage = setup.getBean(FailedMessageDao.class).find().stream().findFirst().get();
        assertThat(failedMessage.getContent(), is(content));
        assertThat(labelsFor(failedMessage), hasItem(label));
        assertThat(failedMessage.getDestination(), is(destination().withBrokerName(equalTo(brokerName)).withQueueName(equalTo(queueName))));
        assertThat(failedMessage.getProperties().get("dlqDeliveryFailureCause").toString(), containsString(RuntimeException.class.getName()));
    }

    private SortedSet<String> labelsFor(FailedMessage failedMessage) {
        return setup.getBean(FailedMessageLabelsDao.class).findLabelsById(failedMessage.getFailedMessageId());
    }

    private void waitToProcessMessage(IntegrationSetup setup) throws InterruptedException {
        while (setup.getBean(FailedMessageDao.class).find().isEmpty()) {
            Thread.sleep(500);
        }
    }
}
