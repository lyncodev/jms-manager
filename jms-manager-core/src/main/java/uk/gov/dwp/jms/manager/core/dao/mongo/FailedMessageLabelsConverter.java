package uk.gov.dwp.jms.manager.core.dao.mongo;

import client.FailedMessageId;
import client.FailedMessageLabels;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static client.FailedMessageId.FAILED_MESSAGE_ID;
import static client.FailedMessageId.fromString;

public class FailedMessageLabelsConverter implements DBObjectWithIdConverter<FailedMessageLabels, FailedMessageId> {

    static final String LABELS = "labels";

    @Override
    public FailedMessageLabels convertToObject(DBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        return new FailedMessageLabels(
                fromString(((BasicDBObject)dbObject.get("_id")).getString(FAILED_MESSAGE_ID)),
                getLabels(dbObject));
    }

    private SortedSet<String> getLabels(DBObject dbObject) {
        List<String> labels = (List<String>) dbObject.get(LABELS);
        if (labels != null) {
            return labels.stream().collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
        }
        return new TreeSet<>();
    }

    @Override
    public BasicDBObject convertFromObject(FailedMessageLabels failedMessageLabels) {
        return createId(failedMessageLabels.getFailedMessageId())
                .append(LABELS, new ArrayList<>(failedMessageLabels.getLabels()));
    }

    @Override
    public BasicDBObject createId(FailedMessageId failedMessageId) {
        return new BasicDBObject("_id", new BasicDBObject(FAILED_MESSAGE_ID, failedMessageId.getId().toString()));
    }
}
