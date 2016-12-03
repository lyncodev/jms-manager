package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.client.DestinationStatistics;
import uk.gov.dwp.jms.manager.client.DestinationStatisticsResource;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DestinationStatisticsControllerTest {

    private static final List<DestinationStatistics> SOME_STATISTICS = Collections.emptyList();
    private static final String SOME_JSON = "[]";
    private final DestinationStatisticsResource destinationStatisticsResource = mock(DestinationStatisticsResource.class);
    private final DestinationStatisticsJsonSerializer jsonSerializer = mock(DestinationStatisticsJsonSerializer.class);

    private final DestinationStatisticsController underTest = new DestinationStatisticsController(destinationStatisticsResource, jsonSerializer);

    @Test
    public void getSummary() throws Exception {
        when(destinationStatisticsResource.getAllStatistics()).thenReturn(SOME_STATISTICS);
        when(jsonSerializer.asJson(SOME_STATISTICS)).thenReturn(SOME_JSON);

        assertThat(underTest.getSummary().getRecords(), equalTo(SOME_JSON));
    }
}