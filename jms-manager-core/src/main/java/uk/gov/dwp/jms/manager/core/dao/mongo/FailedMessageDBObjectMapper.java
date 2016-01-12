package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

import java.util.HashMap;
import java.util.Map;

import static uk.gov.dwp.jms.manager.core.domain.FailedMessageId.fromString;

public class FailedMessageDBObjectMapper implements DBObjectMapper<FailedMessage> {

    static final String CONTENT = "content";
    static final String PROPERTIES = "properties";

    @Override
    public FailedMessage mapObject(DBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        BasicDBObject basicDBObject = (BasicDBObject)dbObject;
        return new FailedMessage(
                fromString(basicDBObject.getString("_id")),
                basicDBObject.getString(CONTENT),
                new HashMap<>((Map<String, Object>)basicDBObject.get(PROPERTIES))
        );
    }

    @Override
    public BasicDBObject mapDBObject(FailedMessage item) {
        return createFailedMessageIdDBObject(item.getFailedMessageId())
                .append(CONTENT, item.getContent())
                .append(PROPERTIES, item.getProperties());
    }

    public BasicDBObject createFailedMessageIdDBObject(FailedMessageId failedMessageId) {
        return new BasicDBObject("_id", failedMessageId.getId().toString());
    }
}
