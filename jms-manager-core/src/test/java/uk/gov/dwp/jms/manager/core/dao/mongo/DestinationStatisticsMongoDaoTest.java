package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.DestinationStatistics;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.jms.manager.core.domain.DestinationMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DBObjectMatcher.hasField;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.DELETED;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.FAILED;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.REPROCESSED;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsMongoDaoTest.FailedMessageStatisticsMatcher.statisticsFor;
import static uk.gov.dwp.jms.manager.core.domain.DestinationMatcher.aDestination;

public class DestinationStatisticsMongoDaoTest extends AbstractMongoDaoTest {

    private static final Destination SOME_DESTINATION = new Destination("broker.name", "queue.name");
    private static final Destination ANOTHER_DESTINATION = new Destination("broker.name", "another.queue.name");
    @Autowired
    private DestinationStatisticsMongoDao underTest;
    @Autowired
    private DBObjectWithIdConverter<DestinationStatistics, Destination> destinationStatisticsConverter;

    @Test
    public void addNewWhenDestinationDoesNotExist() throws Exception {
        underTest.addFailed(SOME_DESTINATION);

        assertThat(collection.findOne(destination(SOME_DESTINATION)), allOf(
                hasField(FAILED, 1),
                hasField(REPROCESSED, 0),
                hasField(DELETED, 0)
        ));
    }

    @Test
    public void testAddNewWhenDestinationExists() throws Exception {
        createDestinationStatistics(new DestinationStatistics(SOME_DESTINATION, 3, 2, 1));
        createDestinationStatistics(new DestinationStatistics(ANOTHER_DESTINATION, 3, 2, 1));

        underTest.addFailed(SOME_DESTINATION);

        assertThat(collection.findOne(destination(SOME_DESTINATION)), allOf(
                hasField(FAILED, 4L),
                hasField(REPROCESSED, 2L),
                hasField(DELETED, 1L)
        ));
        assertThat(collection.findOne(destination(ANOTHER_DESTINATION)), allOf(
                hasField(FAILED, 3L),
                hasField(REPROCESSED, 2L),
                hasField(DELETED, 1L)
        ));
    }

    private WriteResult createDestinationStatistics(DestinationStatistics destinationStatistics) {
        return collection.insert(destinationStatisticsConverter.convertFromObject(destinationStatistics));
    }

    @Test
    public void testReprocess() throws Exception {
        createDestinationStatistics(new DestinationStatistics(SOME_DESTINATION, 3, 2, 1));
        createDestinationStatistics(new DestinationStatistics(ANOTHER_DESTINATION, 3, 2, 1));

        underTest.reprocess(SOME_DESTINATION);

        assertThat(collection.findOne(destination(SOME_DESTINATION)), allOf(
                hasField(FAILED, 2L),
                hasField(REPROCESSED, 3L),
                hasField(DELETED, 1L)
        ));
        assertThat(collection.findOne(destination(ANOTHER_DESTINATION)), allOf(
                hasField(FAILED, 3L),
                hasField(REPROCESSED, 2L),
                hasField(DELETED, 1L)
        ));

    }

    @Test
    public void testDelete() throws Exception {
        createDestinationStatistics(new DestinationStatistics(SOME_DESTINATION, 3, 2, 1));
        createDestinationStatistics(new DestinationStatistics(ANOTHER_DESTINATION, 3, 2, 1));

        underTest.delete(SOME_DESTINATION);

        assertThat(collection.findOne(destination(SOME_DESTINATION)), allOf(
                hasField(FAILED, 2L),
                hasField(REPROCESSED, 2L),
                hasField(DELETED, 2L)
        ));
        assertThat(collection.findOne(destination(ANOTHER_DESTINATION)), allOf(
                hasField(FAILED, 3L),
                hasField(REPROCESSED, 2L),
                hasField(DELETED, 1L)
        ));

    }

    @Test
    public void concurrentAddNewWhenDestinationDoesNotExist() {
        List<Callable<Void>> callables = new ArrayList<>();
        for (int i=0; i<5; i++) {
            callables.add(() -> {
                underTest.addFailed(SOME_DESTINATION);
                return null;
            });
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {
            executorService.invokeAll(callables);
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {

        } finally {
            executorService.shutdownNow();
        }
        assertThat(collection.findOne(destination(SOME_DESTINATION)), allOf(
                hasField(FAILED, 5),
                hasField(REPROCESSED, 0),
                hasField(DELETED, 0)
        ));
    }

    @Test
    public void getAllStatistics() throws Exception {
        underTest.addFailed(new Destination("broker.name", "queue.name"));
        underTest.addFailed(new Destination("broker.name", "another.queue.name"));
        underTest.addFailed(new Destination("another.broker.name", "queue.name"));
        underTest.addFailed(new Destination("another.broker.name", "another.queue.name"));

        assertThat(underTest.getAll(), contains(
                statisticsFor(aDestination().withBrokerName("another.broker.name").withName("another.queue.name")).withFailedCount(1).withReprocessedCount(0).withDeletedCount(0),
                statisticsFor(aDestination().withBrokerName("another.broker.name").withName("queue.name")).withFailedCount(1).withReprocessedCount(0).withDeletedCount(0),
                statisticsFor(aDestination().withBrokerName("broker.name").withName("another.queue.name")).withFailedCount(1).withReprocessedCount(0).withDeletedCount(0),
                statisticsFor(aDestination().withBrokerName("broker.name").withName("queue.name")).withFailedCount(1).withReprocessedCount(0).withDeletedCount(0)
        ));
    }

    static class FailedMessageStatisticsMatcher extends TypeSafeMatcher<DestinationStatistics> {

        private DestinationMatcher destinationMatcher;
        private long failed;
        private long reprocessed;
        private long deleted;

        FailedMessageStatisticsMatcher(DestinationMatcher destinationMatcher, long failed, long reprocessed, long deleted) {
            this.destinationMatcher = destinationMatcher;
            this.failed = failed;
            this.reprocessed = reprocessed;
            this.deleted = deleted;
        }

        static FailedMessageStatisticsMatcher statisticsFor(DestinationMatcher destinationMatcher) {
            return new FailedMessageStatisticsMatcher(destinationMatcher, 0, 0, 0);
        }

        FailedMessageStatisticsMatcher withFailedCount(long failed) {
            this.failed = failed;
            return this;
        }

        FailedMessageStatisticsMatcher withReprocessedCount(long reprocessed) {
            this.reprocessed = reprocessed;
            return this;
        }

        FailedMessageStatisticsMatcher withDeletedCount(long deleted) {
            this.deleted = deleted;
            return this;
        }

        @Override
        protected boolean matchesSafely(DestinationStatistics item) {
            return destinationMatcher.matches(item.getDestination())
                    && failed == item.getFailed()
                    && reprocessed == item.getReprocessed()
                    && deleted == item.getDeleted();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("destination is ").appendDescriptionOf(destinationMatcher)
                    .appendText(", failed is ").appendValue(failed)
                    .appendText(", reprocessed is ").appendValue(reprocessed)
                    .appendText(", deleted is ").appendValue(deleted);
        }
    }

    private BasicDBObject destination(Destination destination) {
        return destinationStatisticsConverter.createId(destination);
    }

    @Override
    protected String getCollectionName() {
        return daoProperties.getCollection().getDestinationStatistics();
    }
}