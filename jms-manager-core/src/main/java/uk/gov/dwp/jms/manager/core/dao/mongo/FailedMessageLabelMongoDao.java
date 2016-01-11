package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.DBCollection;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

public class FailedMessageLabelMongoDao implements FailedMessageLabelDao {

    private final DBCollection collection;
    private final FailedMessageLabelDBObjectMapper dbObjectMapper = new FailedMessageLabelDBObjectMapper(new FailedMessageIdDBObjectMapper());

    public FailedMessageLabelMongoDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public void save(FailedMessageLabel failedMessageLabel) {
        collection.insert(dbObjectMapper.mapDBObject(failedMessageLabel));
    }

    @Override
    public FailedMessageLabel findById(FailedMessageId failedMessageId) {
        return dbObjectMapper.mapObject(collection.findOne(dbObjectMapper.createFailedMessageIdDBObject(failedMessageId)));
    }

    @Override
    public void removeAll(FailedMessageId failedMessageId) {

    }
}
