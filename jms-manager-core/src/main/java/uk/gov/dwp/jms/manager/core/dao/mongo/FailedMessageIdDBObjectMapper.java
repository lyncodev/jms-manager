package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.FAILED_MESSAGE_ID;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.fromString;

public class FailedMessageIdDBObjectMapper implements DBObjectMapper<FailedMessageId> {

    public static final String _ID = "_id";

    @Override
    public FailedMessageId mapObject(DBObject dbObject) {
        return fromString(((BasicDBObject)dbObject).getString(_ID));
    }

    @Override
    public BasicDBObject mapDBObject(FailedMessageId failedMessageId) {
        return new BasicDBObject(_ID, failedMessageId.getId().toString());
    }
}
