package uk.gov.dwp.jms.manager.core.dao.mongo;

import client.Destination;
import client.DestinationStatistics;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DestinationStatisticsConverter implements DBObjectWithIdConverter<DestinationStatistics, Destination> {

    static final String FAILED = "failed";
    static final String REPROCESSED = "reprocessed";
    static final String DELETED = "deleted";

    private final ObjectConverter<Destination, DBObject> destinationDBObjectConverter;

    public DestinationStatisticsConverter(ObjectConverter<Destination, DBObject> destinationDBObjectConverter) {
        this.destinationDBObjectConverter = destinationDBObjectConverter;
    }

    @Override
    public DestinationStatistics convertToObject(DBObject dbObject) {
        BasicDBObject basicDBObject = (BasicDBObject)dbObject;
        return new DestinationStatistics(
                destinationDBObjectConverter.convertToObject((DBObject) basicDBObject.get("_id")),
                basicDBObject.getLong(FAILED),
                basicDBObject.getLong(REPROCESSED),
                basicDBObject.getLong(DELETED)
                );
    }

    @Override
    public DBObject convertFromObject(DestinationStatistics item) {
        return createId(item.getDestination())
                .append(FAILED, item.getFailed())
                .append(REPROCESSED, item.getReprocessed())
                .append(DELETED, item.getDeleted());
    }

    @Override
    public BasicDBObject createId(Destination destination) {
        return new BasicDBObject("_id", destinationDBObjectConverter.convertFromObject(destination));
    }
}
