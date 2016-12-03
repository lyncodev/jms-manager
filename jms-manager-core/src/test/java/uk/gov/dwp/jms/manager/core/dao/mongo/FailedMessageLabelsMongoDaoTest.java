package uk.gov.dwp.jms.manager.core.dao.mongo;

import client.FailedMessageId;
import client.FailedMessageLabels;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static client.FailedMessageId.newFailedMessageId;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FailedMessageLabelsMongoDaoTest extends AbstractMongoDaoTest {

    private static final FailedMessageId FAILED_MESSAGE_ID_1 = newFailedMessageId();
    private static final FailedMessageId FAILED_MESSAGE_ID_2 = newFailedMessageId();

    @Autowired
    private FailedMessageLabelsMongoDao underTest;

    @Test
    public void findByFailedMessageIdReturnsEmptyListWhenNotFound() throws Exception {
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_1), hasSize(0));
    }

    @Test
    public void findByLabelReturnsEmptyListWhenNotFound() throws Exception {
        assertThat(underTest.findByLabel("Label"), hasSize(0));
    }

    @Test
    public void canCreateMultipleLabelsForTheSameFailedMessageId() throws Exception {
        underTest.addLabel(FAILED_MESSAGE_ID_1, "Label One");
        underTest.addLabel(FAILED_MESSAGE_ID_1, "Label Two");

        FailedMessageLabels expected = new FailedMessageLabels(FAILED_MESSAGE_ID_1, newTreeSet("Label One", "Label Two"));
        assertThat(underTest.findByLabel("Label One"), contains(equalTo(expected)));
        assertThat(underTest.findByLabel("Label Two"), contains(equalTo(expected)));
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_1), contains("Label One", "Label Two"));
    }

    @Test
    public void duplicateLabelsForTheSameFailedMessageAreNotSaved() throws Exception {
        underTest.addLabel(FAILED_MESSAGE_ID_1, "A Label");
        underTest.addLabel(FAILED_MESSAGE_ID_1, "A Label");

        FailedMessageLabels expected = new FailedMessageLabels(FAILED_MESSAGE_ID_1, newTreeSet("A Label"));

        assertThat(underTest.findByLabel("A Label"), contains(expected));
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_1), contains("A Label"));
    }

    @Test
    public void canCreateTheSameLabelForDifferentFailedMessages() throws Exception {
        underTest.addLabel(FAILED_MESSAGE_ID_1, "A Label");
        underTest.addLabel(FAILED_MESSAGE_ID_2, "A Label");

        FailedMessageLabels failedMessageLabel1 = new FailedMessageLabels(FAILED_MESSAGE_ID_1, newTreeSet("A Label"));
        FailedMessageLabels failedMessageLabel2 = new FailedMessageLabels(FAILED_MESSAGE_ID_2, newTreeSet("A Label"));

        assertThat(underTest.findByLabel("A Label"), containsInAnyOrder(failedMessageLabel1, failedMessageLabel2));
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_1), contains("A Label"));
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_2), contains("A Label"));
    }

    @Test
    public void removeAll() throws Exception {
        underTest.addLabel(FAILED_MESSAGE_ID_1, "A Label");
        underTest.addLabel(FAILED_MESSAGE_ID_1, "Another Label");
        underTest.addLabel(FAILED_MESSAGE_ID_2, "A Label");

        underTest.remove(FAILED_MESSAGE_ID_1);

        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_1), is(emptyIterable()));
        assertThat(underTest.findLabelsById(FAILED_MESSAGE_ID_2), contains("A Label"));
    }

    @Override
    protected String getCollectionName() {
        return daoProperties.getCollection().getFailedMessageLabel();
    }

    private SortedSet<String> newTreeSet(String...labels) {
        return Arrays.stream(labels).collect(TreeSet::new, Set::add, TreeSet<String>::addAll);
    }
}