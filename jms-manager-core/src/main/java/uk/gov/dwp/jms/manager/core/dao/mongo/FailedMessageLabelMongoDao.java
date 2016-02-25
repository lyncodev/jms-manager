package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelConverter.LABEL;

public class FailedMessageLabelMongoDao implements FailedMessageLabelDao {

    private final DBCollection collection;
    private final FailedMessageLabelConverter failedMessageLabelConverter = new FailedMessageLabelConverter();

    public FailedMessageLabelMongoDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public FailedMessageLabel insert(FailedMessageLabel failedMessageLabel) {
        collection.insert(failedMessageLabelConverter.convertFromObject(failedMessageLabel));
        return failedMessageLabel;
    }

    @Override
    public List<FailedMessageLabel> findById(FailedMessageId failedMessageId) {
        return toList(collection.find(new BasicDBObject(FAILED_MESSAGE_ID, failedMessageId.getId().toString())).sort(new BasicDBObject(LABEL, 1)));
    }

    @Override
    public List<FailedMessageLabel> findByLabel(String label) {
        return toList(collection.find(new BasicDBObject(LABEL, label)).sort(new BasicDBObject(FAILED_MESSAGE_ID, 1)));
    }

    @Override
    public void remove(FailedMessageId failedMessageId, String label) {
        collection.remove(new BasicDBObject(FAILED_MESSAGE_ID, failedMessageId.getId().toString()).append(LABEL, label));
    }

    @Override
    public int removeAll(FailedMessageId failedMessageId) {
        return collection.remove(new BasicDBObject(FAILED_MESSAGE_ID, failedMessageId.getId().toString())).getN();
    }

    private List<FailedMessageLabel> toList(DBCursor dbCursor) {
        Set<FailedMessageLabel> labels = new HashSet<>();
        while (dbCursor.hasNext()) {
            labels.add(failedMessageLabelConverter.convertToObject(dbCursor.next()));
        }
        return labels.stream().collect(Collectors.toList());
    }
}
