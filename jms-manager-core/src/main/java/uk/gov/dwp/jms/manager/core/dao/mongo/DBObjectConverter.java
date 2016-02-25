package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.DBObject;

public interface DBObjectConverter<T> extends ObjectConverter<T, DBObject> {

}