package uk.gov.dwp.jms.manager.core.service;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.FailedMessage;
import uk.gov.dwp.jms.manager.core.client.FailedMessageId;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelDao;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class FailedMessageServiceImplTest {

    private static final FailedMessageId FAILED_MESSAGE_ID = FailedMessageId.newFailedMessageId();
    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private final FailedMessageLabelDao failedMessageLabelDao = mock(FailedMessageLabelDao.class);
    private final DestinationStatisticsDao destinationStatisticsDao = mock(DestinationStatisticsDao.class);

    private final FailedMessageServiceImpl underTest = new FailedMessageServiceImpl(failedMessageDao, failedMessageLabelDao, destinationStatisticsDao);
    private final FailedMessage failedMessage = mock(FailedMessage.class);

    @Test
    public void testCreate() throws Exception {
        Destination destination = mock(Destination.class);
        when(failedMessage.getDestination()).thenReturn(destination);

        underTest.create(failedMessage);

        verify(failedMessageDao).insert(failedMessage);
        verify(destinationStatisticsDao).addFailed(destination);
    }

    @Test
    public void testFindById() throws Exception {
        when(failedMessageDao.findById(FAILED_MESSAGE_ID)).thenReturn(failedMessage);

        assertThat(underTest.getFailedMessage(FAILED_MESSAGE_ID), is(failedMessage));

        verify(failedMessageDao).findById(FAILED_MESSAGE_ID);
    }

    @Test
    public void testFindAllFailedMessages() throws Exception {
        List failedMessages = mock(List.class);
        when(failedMessageDao.find()).thenReturn(failedMessages);

        assertThat(underTest.getFailedMessages(), is(failedMessages));

        verify(failedMessageDao).find();
    }

    @Test
    public void testReprocess() throws Exception {
        Destination destination = mock(Destination.class);
        when(failedMessage.getDestination()).thenReturn(destination);
        when(failedMessageDao.findById(FAILED_MESSAGE_ID)).thenReturn(failedMessage);
        underTest.reprocess(FAILED_MESSAGE_ID);

        verify(failedMessageDao).remove(FAILED_MESSAGE_ID);
        verify(failedMessageLabelDao).removeAll(FAILED_MESSAGE_ID);
        verify(destinationStatisticsDao).reprocess(destination);
    }
}