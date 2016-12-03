package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.client.Destination;
import uk.gov.dwp.jms.manager.client.DestinationStatistics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DestinationStatisticsJsonSerializer {

    private final ObjectMapper objectMapper;

    public DestinationStatisticsJsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String asJson(List<DestinationStatistics> destinationStatistics) {
        AtomicInteger recid = new AtomicInteger(1);
        List<DestinationStatisticsView> views = destinationStatistics
                .stream()
                .map(ds -> new DestinationStatisticsView(Integer.toString(recid.getAndIncrement()), ds))
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(views);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    static class DestinationStatisticsView {

        private final String recid;
        private final DestinationStatistics destinationStatistics;

        public DestinationStatisticsView(String recid, DestinationStatistics destinationStatistics) {;
            this.recid = recid;
            this.destinationStatistics = destinationStatistics;
        }

        public String getRecid() {
            return recid;
        }

        public Destination getDestination() {
            return destinationStatistics.getDestination();
        }

        public long getFailed() {
            return destinationStatistics.getFailed();
        }

        public long getReprocessed() {
            return destinationStatistics.getReprocessed();
        }

        public long getDeleted() {
            return destinationStatistics.getDeleted();
        }

        public long getTotal() {
            return getFailed() + getReprocessed() + getDeleted();
        }
    }
}
