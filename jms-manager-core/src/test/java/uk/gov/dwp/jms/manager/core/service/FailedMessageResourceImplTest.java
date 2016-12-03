package uk.gov.dwp.jms.manager.core.service;

import client.Destination;
import client.FailedMessage;
import client.FailedMessageBuilder;
import client.FailedMessageId;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;
import uk.gov.dwp.jms.manager.core.service.resources.FailedMessageResourceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.dwp.jms.manager.core.domain.FailedMessageMatcher.aFailedMessage;

public class FailedMessageResourceImplTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();
    private static final FailedMessageId ANOTHER_FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();

    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private final FailedMessageLabelsDao failedMessageLabelsDao = mock(FailedMessageLabelsDao.class);

    private final FailedMessageResourceImpl underTest = new FailedMessageResourceImpl(failedMessageDao, failedMessageLabelsDao);
    private final Destination destination = mock(Destination.class);
    private final FailedMessage failedMessage = FailedMessageBuilder.aFailedMessage().withFailedMessageId(FAILED_MESSAGE_ID).withDestination(destination).build();


    @Test
    public void testFindById() throws Exception {
        when(failedMessageDao.findById(FAILED_MESSAGE_ID)).thenReturn(failedMessage);
        when(failedMessageLabelsDao.findLabelsById(FAILED_MESSAGE_ID)).thenReturn(treeSet("A Label"));

        FailedMessage failedMessage = underTest.getFailedMessage(FAILED_MESSAGE_ID);
        assertThat(failedMessage, is(aFailedMessage()
                .withFailedMessageId(equalTo(FAILED_MESSAGE_ID))
                .withLabels(contains("A Label"))
        ));

        verify(failedMessageDao).findById(FAILED_MESSAGE_ID);
        verify(failedMessageLabelsDao).findLabelsById(FAILED_MESSAGE_ID);
    }

    private TreeSet<String> treeSet(String...labels) {
        return Arrays.stream(labels).collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
    }

    @Test
    public void testFindAllFailedMessages() throws Exception {
        when(failedMessageDao.find()).thenReturn(Collections.singletonList(failedMessage));
        when(failedMessageLabelsDao.findLabelsById(FAILED_MESSAGE_ID)).thenReturn(treeSet("A Label"));

        assertThat(underTest.getFailedMessages(), contains(aFailedMessage().withFailedMessageId(equalTo(FAILED_MESSAGE_ID)).withLabels(contains("A Label"))));

        verify(failedMessageDao).find();
        verify(failedMessageLabelsDao).findLabelsById(FAILED_MESSAGE_ID);
    }
}