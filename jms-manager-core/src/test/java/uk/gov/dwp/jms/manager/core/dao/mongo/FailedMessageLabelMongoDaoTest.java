package uk.gov.dwp.jms.manager.core.dao.mongo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.client.FailedMessageLabel;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static uk.gov.dwp.jms.manager.core.client.FailedMessageId.newFailedMessageId;

public class FailedMessageLabelMongoDaoTest extends AbstractMongoDaoTest {

    @Autowired
    private FailedMessageLabelMongoDao underTest;

    @Test
    public void findByFailedMessageIdReturnsEmptyListWhenNotFound() throws Exception {
        assertThat(underTest.findById(newFailedMessageId()), equalTo(new ArrayList<>()));
    }

    @Test
    public void findByLabelReturnsEmptyListWhenNotFound() throws Exception {
        assertThat(underTest.findByLabel("Label"), equalTo(new ArrayList<>()));
    }

    @Test
    public void canCreateMultipleLabelsForTheSameFailedMessageId() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        FailedMessageLabel failedMessageLabel1 = underTest.insert(new FailedMessageLabel(failedMessageId, "Label One"));
        FailedMessageLabel failedMessageLabel2 = underTest.insert(new FailedMessageLabel(failedMessageId, "Label Two"));

        assertThat(underTest.findByLabel("Label One"), contains(failedMessageLabel1));
        assertThat(underTest.findByLabel("Label Two"), contains(failedMessageLabel2));
        assertThat(underTest.findById(failedMessageId), contains(failedMessageLabel1, failedMessageLabel2));
    }

    @Test
    public void duplicateLabelsForTheSameFailedMessageAreNotReturned() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        FailedMessageLabel failedMessageLabel1 = underTest.insert(new FailedMessageLabel(failedMessageId, "A Label"));
        underTest.insert(new FailedMessageLabel(failedMessageId, "A Label"));

        assertThat(underTest.findByLabel("A Label"), contains(failedMessageLabel1));
        assertThat(underTest.findById(failedMessageId), contains(failedMessageLabel1));
    }

    @Test
    public void canCreateTheSameLabelForDifferentFailedMessages() throws Exception {
        FailedMessageLabel failedMessageLabel1 = underTest.insert(new FailedMessageLabel(newFailedMessageId(), "A Label"));
        FailedMessageLabel failedMessageLabel2 = underTest.insert(new FailedMessageLabel(newFailedMessageId(), "A Label"));

        assertThat(underTest.findByLabel("A Label"), containsInAnyOrder(failedMessageLabel1, failedMessageLabel2));
        assertThat(underTest.findById(failedMessageLabel1.getFailedMessageId()), contains(failedMessageLabel1));
        assertThat(underTest.findById(failedMessageLabel2.getFailedMessageId()), contains(failedMessageLabel2));
    }

    @Test
    public void removeAll() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessageLabel(failedMessageId, "A Label"));
        underTest.insert(new FailedMessageLabel(failedMessageId, "Another Label"));

        underTest.removeAll(failedMessageId);

        assertThat(underTest.findById(failedMessageId), is(emptyIterable()));
    }

    @Test
    public void remove() throws Exception {
        FailedMessageId failedMessageId = newFailedMessageId();
        underTest.insert(new FailedMessageLabel(failedMessageId, "A Label"));
        underTest.insert(new FailedMessageLabel(failedMessageId, "A Label"));
        FailedMessageLabel failedMessageLabel1 = underTest.insert(new FailedMessageLabel(failedMessageId, "Another Label"));
        FailedMessageLabel failedMessageLabel2 = underTest.insert(new FailedMessageLabel(newFailedMessageId(), "A Label"));

        underTest.remove(failedMessageId, "A Label");

        assertThat(underTest.findById(failedMessageId), contains(failedMessageLabel1));
        assertThat(underTest.findByLabel("A Label"), contains(failedMessageLabel2));
    }

    @Override
    protected String getCollectionName() {
        return daoProperties.getCollection().getFailedMessageLabel();
    }
}