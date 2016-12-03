package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.DestinationStatistics;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.DELETED;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.FAILED;
import static uk.gov.dwp.jms.manager.core.dao.mongo.DestinationStatisticsConverter.REPROCESSED;

public class DestinationStatisticsMongoDao implements DestinationStatisticsDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationStatisticsMongoDao.class);

    private final DBCollection collection;
    private final DBObjectWithIdConverter<DestinationStatistics, Destination> dbObjectConverter;

    public DestinationStatisticsMongoDao(DBCollection collection, DBObjectWithIdConverter<DestinationStatistics, Destination> dbObjectConverter) {
        this.collection = collection;
        this.dbObjectConverter = dbObjectConverter;
    }

    @Override
    public void addFailed(Destination destination) {
        BasicDBObject destinationDBObject = dbObjectConverter.createId(destination);
        synchronized (this) {
            DBObject dbObject = collection.findOne(destinationDBObject);
            if (dbObject == null) {
                LOGGER.debug("New destination detected: {}", destination);
                try {
                    collection.insert(
                            destinationDBObject
                                    .append(FAILED, 1)
                                    .append(REPROCESSED, 0)
                                    .append(DELETED, 0),
                            WriteConcern.ACKNOWLEDGED
                    );
                    return;
                } catch (DuplicateKeyException ignore) {
                    LOGGER.debug("Destination: {} already exists", destination);
                }
            }
        }
        LOGGER.debug("Updating existing destination: {}", destination);
        WriteResult writeResult = collection.update(
                destinationDBObject,
                new BasicDBObject("$inc", new BasicDBObject(FAILED, 1)),
                false, false, WriteConcern.ACKNOWLEDGED
        );
        LOGGER.debug("Write Result: {}", writeResult.getN());
    }

    @Override
    public void reprocess(Destination destination) {
        collection.update(
                dbObjectConverter.createId(destination),
                new BasicDBObject()
                        .append("$inc", new BasicDBObject(FAILED, -1).append(REPROCESSED, 1))
        );
    }

    @Override
    public void delete(Destination destination) {
        collection.update(
                dbObjectConverter.createId(destination),
                new BasicDBObject()
                        .append("$inc", new BasicDBObject(FAILED, -1).append(DELETED, 1))
        );
    }

    @Override
    public List<DestinationStatistics> getAll() {
        List<DestinationStatistics> allDestinationStatistics = new ArrayList<>();
        try (DBCursor dbCursor = collection.find().sort(new BasicDBObject("_id.brokerName", 1).append("_id.name", 1))) {
            while (dbCursor.hasNext()) {
                allDestinationStatistics.add(dbObjectConverter.convertToObject(dbCursor.next()));
            }
        }
        return allDestinationStatistics;
    }

}
