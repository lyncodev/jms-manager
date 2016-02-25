package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;

import java.util.ArrayList;
import java.util.List;

public class FailedMessageMongoDao implements FailedMessageDao {

    private final DBCollection collection;
    private final FailedMessageConverter failedMessageConverter;

    public FailedMessageMongoDao(DBCollection collection, FailedMessageConverter failedMessageConverter) {
        this.collection = collection;
        this.failedMessageConverter = failedMessageConverter;
    }

    @Override
    public void insert(FailedMessage failedMessage) {
        collection.insert(failedMessageConverter.convertFromObject(failedMessage));
    }

    @Override
    public FailedMessage findById(FailedMessageId failedMessageId) {
        DBObject failedMessage = collection.findOne(failedMessageConverter.createFailedMessageIdDBObject(failedMessageId));
        return failedMessageConverter.convertToObject(failedMessage);
    }

    @Override
    public int remove(FailedMessageId failedMessageId) {
        return collection.remove(failedMessageConverter.createFailedMessageIdDBObject(failedMessageId)).getN();
    }

    @Override
    public List<FailedMessage> find() {
        List<FailedMessage> failedMessages = new ArrayList<>();
        DBCursor dbCursor = collection.find();
        while (dbCursor.hasNext()) {
            failedMessages.add(failedMessageConverter.convertToObject(dbCursor.next()));
        }
        return failedMessages;
    }
}
