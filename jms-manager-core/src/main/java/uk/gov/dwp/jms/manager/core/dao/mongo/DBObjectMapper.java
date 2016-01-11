package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface DBObjectMapper<T> {

    T mapObject(DBObject dbObject);

    BasicDBObject mapDBObject(T item);
}
