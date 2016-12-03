package uk.gov.dwp.jms.manager.core.dao.mongo;

import uk.gov.dwp.jms.manager.client.FailedMessageId;
import uk.gov.dwp.jms.manager.client.FailedMessageLabels;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static uk.gov.dwp.jms.manager.client.FailedMessageLabels.aFailedMessageWithLabels;
import static java.util.Optional.ofNullable;
import static uk.gov.dwp.jms.manager.core.dao.mongo.FailedMessageLabelsConverter.LABELS;

public class FailedMessageLabelsMongoDao implements FailedMessageLabelsDao {

    private final DBCollection collection;
    private final FailedMessageLabelsConverter failedMessageLabelsConverter = new FailedMessageLabelsConverter();

    public FailedMessageLabelsMongoDao(DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public void addLabel(FailedMessageId failedMessageId, String label) {
        SortedSet<String> labels = findLabelsById(failedMessageId);
        if (labels.add(label)) {
            setLabels(failedMessageId, labels);
        }
    }

    @Override
    public void setLabels(FailedMessageId failedMessageId, Set<String> labels) {
        BasicDBObject id = failedMessageLabelsConverter.createId(failedMessageId);
        collection.update(id, failedMessageLabelsConverter.convertFromObject(aFailedMessageWithLabels(failedMessageId, labels)), true, false);
    }

    @Override
    public SortedSet<String> findLabelsById(FailedMessageId failedMessageId) {
        DBObject dbObject = collection.findOne(failedMessageLabelsConverter.createId(failedMessageId));
        return ofNullable(failedMessageLabelsConverter.convertToObject(dbObject)).map(FailedMessageLabels::getLabels).orElse(new TreeSet<>());
    }

    @Override
    public Set<FailedMessageLabels> findByLabel(String label) {
        return toSet(collection.find(new BasicDBObject(LABELS, label)));
    }

    @Override
    public int remove(FailedMessageId failedMessageId) {
        return collection.remove(failedMessageLabelsConverter.createId(failedMessageId)).getN();
    }

    private Set<FailedMessageLabels> toSet(DBCursor dbCursor) {
        Set<FailedMessageLabels> labels = new HashSet<>();
        while (dbCursor.hasNext()) {
            labels.add(failedMessageLabelsConverter.convertToObject(dbCursor.next()));
        }
        return labels;
    }
}
