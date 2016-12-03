package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.Destination;
import com.mongodb.DBObject;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.core.domain.DestinationMatcher.aDestination;

public class DestinationDBObjectConverterTest {

    private final DestinationDBObjectConverter underTest = new DestinationDBObjectConverter();

    @Test
    public void testConvertQueueToDBObjectAndBack() throws Exception {
        DBObject basicDBObject = underTest.convertFromObject(new Destination("broker.name", "queue.name"));

        assertThat(underTest.convertToObject(basicDBObject), is(aDestination().withBrokerName("broker.name").withName("queue.name")));
    }
}