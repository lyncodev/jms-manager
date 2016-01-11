package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

public class FailedMessageMongoDao implements FailedMessageDao {

    private final DBCollection collection;
    private final FailedMessageDBObjectMapper dbObjectMapper = new FailedMessageDBObjectMapper(new FailedMessageIdDBObjectMapper());

    public FailedMessageMongoDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public void insert(FailedMessage failedMessage) {
        collection.insert(dbObjectMapper.mapDBObject(failedMessage));
    }

    @Override
    public FailedMessage findById(FailedMessageId failedMessageId) {
        return dbObjectMapper.mapObject(collection.findOne(dbObjectMapper.createFailedMessageIdDBObject(failedMessageId)));
    }

    @Override
    public int remove(FailedMessageId failedMessageId) {
        return collection.remove(dbObjectMapper.createFailedMessageIdDBObject(failedMessageId)).getN();
    }
}
