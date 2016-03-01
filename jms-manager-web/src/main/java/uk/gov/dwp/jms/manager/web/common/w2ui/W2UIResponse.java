package uk.gov.dwp.jms.manager.web.common.w2ui;

import java.util.List;

public class W2UIResponse<T> {

    private final List<T> records;

    public W2UIResponse(List<T> records) {
        this.records = records;
    }

    public String getStatus() {
        return "success";
    }

    public long getTotal() {
        return records.size();
    }

    public List<T> getRecords() {
        return records;
    }
}
