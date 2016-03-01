package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;

import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.fromString;

public class FailedMessageLabelConverter implements DBObjectWithIdConverter<FailedMessageLabel, ObjectId> {

    static final String LABEL = "label";

    @Override
    public FailedMessageLabel convertToObject(DBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        BasicDBObject basicDBObject = (BasicDBObject)dbObject;
        return new FailedMessageLabel(
                fromString(basicDBObject.getString(FAILED_MESSAGE_ID)),
                basicDBObject.getString(LABEL)
        );
    }

    @Override
    public BasicDBObject convertFromObject(FailedMessageLabel failedMessageLabel) {
        return createId(new ObjectId())
                .append(FAILED_MESSAGE_ID, failedMessageLabel.getFailedMessageId().getId().toString())
                .append(LABEL, failedMessageLabel.getLabel());
    }

    @Override
    public BasicDBObject createId(ObjectId objectId) {
        return new BasicDBObject("_id", objectId);
    }
}
