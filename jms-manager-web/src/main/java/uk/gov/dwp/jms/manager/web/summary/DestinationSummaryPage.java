package uk.gov.dwp.jms.manager.web.summary;

import uk.gov.dwp.jms.manager.web.common.Page;

public class DestinationSummaryPage extends Page {

    private final String records;

    public DestinationSummaryPage(String records) {
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
