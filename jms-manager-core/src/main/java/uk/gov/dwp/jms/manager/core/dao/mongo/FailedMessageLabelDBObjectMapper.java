package uk.gov.dwp.jms.manager.core.dao.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageId;
import uk.gov.dwp.jms.manager.core.domain.FailedMessageLabel;

public class FailedMessageLabelDBObjectMapper implements DBObjectMapper<FailedMessageLabel> {

    static final String LABEL = "label";

    private final FailedMessageIdDBObjectMapper failedMessageIdDBObjectMapper;

    public FailedMessageLabelDBObjectMapper(FailedMessageIdDBObjectMapper failedMessageIdDBObjectMapper) {
        this.failedMessageIdDBObjectMapper = failedMessageIdDBObjectMapper;
    }

    @Override
    public FailedMessageLabel mapObject(DBObject dbObject) {
        return new FailedMessageLabel(
                failedMessageIdDBObjectMapper.mapObject(dbObject),
                ((BasicDBObject)dbObject).getString(LABEL)
        );
    }

    @Override
    public BasicDBObject mapDBObject(FailedMessageLabel failedMessageLabel) {
        return failedMessageIdDBObjectMapper.mapDBObject(failedMessageLabel.getFailedMessageId())
                .append(LABEL, failedMessageLabel.getLabel());
    }

    public BasicDBObject createFailedMessageIdDBObject(FailedMessageId failedMessageId) {
        return failedMessageIdDBObjectMapper.mapDBObject(failedMessageId);
    }
}
