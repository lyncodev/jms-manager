package uk.gov.dwp.jms.manager;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import org.junit.After;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.classification.action.DelayedFailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.action.MoveToQueueFailedMessageAction;
import uk.gov.dwp.jms.manager.core.classification.action.delay.RelativeDelay;
import uk.gov.dwp.jms.manager.core.classification.predicate.BrokerPredicate;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.gov.dwp.jms.manager.classification.MessageClassifierBuilder.messageClassifierBuilder;
import static uk.gov.dwp.jms.manager.core.client.DestinationMatcherBuilder.destination;

public class DelayedMessageMoveTest {
    private IntegrationSetup setup;

    @After
    public void tearDown() throws Exception {
        setup.stop();
    }


    @Test
    public void delayedMessageMove() throws Exception {

        // given
        String content = "message";
        String queueName = "queue";
        String brokerName = "internal";
        String label = "PROBLEM";
        String destinationQueue = "destinationQueue";

        setup = IntegrationSetupBuilder.integrationSetup()
                .withBroker(brokerName)
                .withMessageClassifier(messageClassifierBuilder()
                        .withPredicate(new BrokerPredicate(brokerName))
                        .withAction(new DelayedFailedMessageAction(
                                new RelativeDelay(TimeUnit.SECONDS, 5),
                                new MoveToQueueFailedMessageAction(destinationQueue)
                        ))
                        .build())
                .build().start();

        // when
        setup.broker(brokerName).sendAndDeadLetterMessage(queueName, content, RuntimeException::new);
        waitToProcessMessage(setup);

        // then
        FailedMessage failedMessage = setup.getBean(FailedMessageDao.class).find().stream().findFirst().get();
        assertThat(failedMessage.getContent(), is(content));
        assertThat(failedMessage.getDestination(), is(destination().withBrokerName(equalTo(brokerName)).withQueueName(equalTo(queueName))));
        assertThat(failedMessage.getProperties().get("dlqDeliveryFailureCause").toString(), containsString(RuntimeException.class.getName()));
        // after 5 seconds
        Thread.sleep(6000);
        assertTrue(setup.getBean(FailedMessageDao.class).find().isEmpty());
        assertThat(setup.broker(brokerName).countMessages().get(destinationQueue), is(1L));
    }

    private void waitToProcessMessage(IntegrationSetup setup) throws InterruptedException {
        while (setup.getBean(FailedMessageDao.class).find().isEmpty()) {
            Thread.sleep(500);
        }
    }
}
