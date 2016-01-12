package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.fromString;

public class FailedMessageLabelDBObjectMapper implements DBObjectMapper<FailedMessageLabel> {

    static final String LABEL = "label";

    @Override
    public FailedMessageLabel mapObject(DBObject dbObject) {
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
    public BasicDBObject mapDBObject(FailedMessageLabel failedMessageLabel) {
        return new BasicDBObject(FAILED_MESSAGE_ID, failedMessageLabel.getFailedMessageId().getId().toString())
                .append(LABEL, failedMessageLabel.getLabel());
    }
}
