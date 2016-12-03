package uk.gov.dwp.jms.manager.core.service.resources;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.FailedMessage;
import uk.gov.dwp.jms.manager.client.FailedMessageId;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageDao;
import uk.gov.dwp.jms.manager.core.service.messages.FailedMessageService;

import static java.util.Arrays.asList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FailedMessageRemoveResourceImplTest {
    private final FailedMessageDao failedMessageDao = mock(FailedMessageDao.class);
    private final FailedMessageService failedMessageService = mock(FailedMessageService.class);
    private FailedMessageRemoveResourceImpl underTest = new FailedMessageRemoveResourceImpl(
            failedMessageDao, failedMessageService
    );

    @Test
    public void removeSingleById() throws Exception {
        FailedMessageId failedMessageId = FailedMessageId.newFailedMessageId();
        FailedMessage failedMessage = mock(FailedMessage.class);
        Destination destination = mock(Destination.class);

        given(failedMessageDao.findById(failedMessageId)).willReturn(failedMessage);
        given(failedMessage.getFailedMessageId()).willReturn(failedMessageId);
        given(failedMessage.getDestination()).willReturn(destination);

        underTest.remove(failedMessageId);

        verify(failedMessageService).remove(failedMessage);
    }

    @Test
    public void removeBulk() throws Exception {
        FailedMessageId failedMessageId1 = FailedMessageId.newFailedMessageId();
        FailedMessage failedMessage1 = mock(FailedMessage.class);
        Destination destination1 = mock(Destination.class);
        FailedMessageId failedMessageId2 = FailedMessageId.newFailedMessageId();
        FailedMessage failedMessage2 = mock(FailedMessage.class);
        Destination destination2 = mock(Destination.class);

        given(failedMessageDao.findById(failedMessageId1)).willReturn(failedMessage1);
        given(failedMessage1.getFailedMessageId()).willReturn(failedMessageId1);
        given(failedMessage1.getDestination()).willReturn(destination1);

        given(failedMessageDao.findById(failedMessageId2)).willReturn(failedMessage2);
        given(failedMessage2.getFailedMessageId()).willReturn(failedMessageId2);
        given(failedMessage2.getDestination()).willReturn(destination2);

        underTest.remove(asList(failedMessageId1, failedMessageId2));

        verify(failedMessageService).remove(failedMessage1);
        verify(failedMessageService).remove(failedMessage2);
    }
}