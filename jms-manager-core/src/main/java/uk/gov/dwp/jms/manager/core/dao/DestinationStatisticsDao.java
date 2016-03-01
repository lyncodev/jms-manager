package uk.gov.dwp.jms.manager.core.dao;

import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.DestinationStatistics;

import java.util.List;

public interface DestinationStatisticsDao {

    void addFailed(Destination destination);

    void reprocess(Destination destination);

    void delete(Destination destination);

    List<DestinationStatistics> getAll();
}
