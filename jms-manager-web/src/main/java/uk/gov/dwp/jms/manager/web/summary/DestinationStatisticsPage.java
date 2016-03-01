package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.web.common.Page;

public class DestinationStatisticsPage extends Page {

    private final String records;

    public DestinationStatisticsPage(String records) {
        super("destination-statistics.mustache");
        this.records = records;
    }

    public String getFailedMessageListUrl() {
        return "/web/failed-messages";
    }

    public String getRecords() {
        return records;
    }
}
