package uk.gov.dwp.jms.manager.core.dao.mongo;

import client.Destination;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DestinationDBObjectConverter implements DBObjectConverter<Destination> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Destination convertToObject(DBObject dbObject) {
        return objectMapper.convertValue(dbObject, Destination.class);
    }

    @Override
    public DBObject convertFromObject(Destination item) {
        return objectMapper.convertValue(item, BasicDBObject.class);
    }
}