package uk.gov.dwp.jms.manager.web.summary;

import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.DestinationStatistics;
import uk.gov.dwp.jms.manager.core.client.DestinationStatisticsResource;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DestinationSummaryControllerTest {

    private static final List<DestinationStatistics> SOME_STATISTICS = Collections.emptyList();
    private static final String SOME_JSON = "[]";
    private final DestinationStatisticsResource destinationStatisticsResource = mock(DestinationStatisticsResource.class);
    private final DestinationSummaryJsonSerializer jsonSerializer = mock(DestinationSummaryJsonSerializer.class);

    private final DestinationSummaryController underTest = new DestinationSummaryController(destinationStatisticsResource, jsonSerializer);

    @Test
    public void getSummary() throws Exception {
        when(destinationStatisticsResource.getAllStatistics()).thenReturn(SOME_STATISTICS);
        when(jsonSerializer.asJson(SOME_STATISTICS)).thenReturn(SOME_JSON);

        assertThat(underTest.getSummary().getRecords(), equalTo(SOME_JSON));
    }
}