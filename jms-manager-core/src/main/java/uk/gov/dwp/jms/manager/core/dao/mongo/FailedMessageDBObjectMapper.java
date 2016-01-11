package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.domain.FailedMessage;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;

import java.util.HashMap;
import java.util.Map;

public class FailedMessageDBObjectMapper implements DBObjectMapper<FailedMessage> {

    static final String CONTENT = "content";
    static final String PROPERTIES = "properties";

    private final FailedMessageIdDBObjectMapper failedMessageIdDBObjectMapper;

    public FailedMessageDBObjectMapper(FailedMessageIdDBObjectMapper failedMessageIdDBObjectMapper) {
        this.failedMessageIdDBObjectMapper = failedMessageIdDBObjectMapper;
    }

    @Override
    public FailedMessage mapObject(DBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        return new FailedMessage(
                failedMessageIdDBObjectMapper.mapObject(dbObject),
                ((BasicDBObject)dbObject).getString(CONTENT),
                new HashMap<>((Map<String, Object>)dbObject.get(PROPERTIES))
        );
    }

    @Override
    public BasicDBObject mapDBObject(FailedMessage item) {
        return failedMessageIdDBObjectMapper.mapDBObject(item.getFailedMessageId())
                .append(CONTENT, item.getContent())
                .append(PROPERTIES, item.getProperties());
    }

    public BasicDBObject createFailedMessageIdDBObject(FailedMessageId failedMessageId) {
        return failedMessageIdDBObjectMapper.mapDBObject(failedMessageId);
    }
}
