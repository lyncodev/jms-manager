package uk.gov.dwp.jms.manager.web.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import uk.gov.dwp.jms.manager.core.client.Destination;
import uk.gov.dwp.jms.manager.core.client.DestinationStatistics;
import uk.gov.dwp.jms.manager.web.configuration.JacksonConfiguration;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.valid4j.matchers.jsonpath.JsonPathMatchers.hasJsonPath;

public class DestinationStatisticsJsonSerializerTest {

    private final ObjectMapper objectMapper = new JacksonConfiguration().objectMapper();
    private final DestinationStatisticsJsonSerializer underTest = new DestinationStatisticsJsonSerializer(objectMapper);

    @Test
    public void serializeEmptyListToJson() throws Exception {
        assertThat(underTest.asJson(Collections.EMPTY_LIST), equalTo("[]"));
    }

    @Test
    public void serializeDestinationStatisticsToJson() throws Exception {

        String json = underTest.asJson(Arrays.asList(
                new DestinationStatistics(new Destination("internal", "uc.system.core"), 1, 2, 3),
                new DestinationStatistics(new Destination("internal", "uc.agent.core"), 4, 5, 6)
        ));
        assertThat(json, allOf(
                hasJsonPath("$.[0].recid", equalTo("1")),
                hasJsonPath("$.[0].destination.brokerName", equalTo("internal")),
                hasJsonPath("$.[0].destination.name", equalTo("uc.system.core")),
                hasJsonPath("$.[0].failed", equalTo(1)),
                hasJsonPath("$.[0].reprocessed", equalTo(2)),
                hasJsonPath("$.[0].deleted", equalTo(3)),
                hasJsonPath("$.[0].total", equalTo(6)),

                hasJsonPath("$.[1].recid", equalTo("2")),
                hasJsonPath("$.[1].destination.brokerName", equalTo("internal")),
                hasJsonPath("$.[1].destination.name", equalTo("uc.agent.core")),
                hasJsonPath("$.[1].failed", equalTo(4)),
                hasJsonPath("$.[1].reprocessed", equalTo(5)),
                hasJsonPath("$.[1].deleted", equalTo(6)),
                hasJsonPath("$.[1].total", equalTo(15))
        ));
    }
}