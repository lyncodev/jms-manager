package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageId;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.QueryOperators.EXISTS;

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
        DBObject failedMessage = collection.findOne(failedMessageConverter.createId(failedMessageId));
        return failedMessageConverter.convertToObject(failedMessage);
    }

    @Override
    public int delete(FailedMessageId failedMessageId) {
        collection.insert(new BasicDBObject()
                .append("_removed", collection.findOne(failedMessageConverter.createId(failedMessageId)))
                .append("_removedDateTime", new Date()));
        return collection.remove(failedMessageConverter.createId(failedMessageId)).getN();
    }

    @Override
    public List<FailedMessage> find() {
        List<FailedMessage> failedMessages = new ArrayList<>();
        DBCursor dbCursor = collection.find(new BasicDBObject("_removedDateTime", new BasicDBObject(EXISTS, false)));
        while (dbCursor.hasNext()) {
            failedMessages.add(failedMessageConverter.convertToObject(dbCursor.next()));
        }
        return failedMessages;
    }
}
