package uk.gov.dwp.jms.manager.core.service;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DestinationStatisticsResourceImplTest {

    private final DestinationStatisticsDao destinationStatisticsDao = mock(DestinationStatisticsDao.class);

    private final DestinationStatisticsResourceImpl underTest = new DestinationStatisticsResourceImpl(destinationStatisticsDao);

    @Test
    public void getAllDelegatesToDao() throws Exception {
        underTest.getAllStatistics();
        verify(destinationStatisticsDao).getAll();
    }
}