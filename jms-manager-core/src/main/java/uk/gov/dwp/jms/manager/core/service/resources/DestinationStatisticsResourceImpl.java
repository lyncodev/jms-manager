package uk.gov.dwp.jms.manager.core.service.resources;

import client.DestinationStatistics;
import client.DestinationStatisticsResource;
import uk.gov.dwp.jms.manager.core.dao.DestinationStatisticsDao;

import java.util.List;

public class DestinationStatisticsResourceImpl implements DestinationStatisticsResource {

    private final DestinationStatisticsDao destinationStatisticsDao;

    public DestinationStatisticsResourceImpl(DestinationStatisticsDao destinationStatisticsDao) {
        this.destinationStatisticsDao = destinationStatisticsDao;
    }

    @Override
    public List<DestinationStatistics> getAllStatistics() {
        return destinationStatisticsDao.getAll();
    }
}
