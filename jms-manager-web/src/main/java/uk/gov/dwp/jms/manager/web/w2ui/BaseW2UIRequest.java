package uk.gov.dwp.jms.manager.web.w2ui;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BaseW2UIRequest {

    private final List<String> selected;
    private final String cmd;
    private final Integer limit;
    private final Integer offset;

    public BaseW2UIRequest(@JsonProperty("cmd") String cmd,
                           @JsonProperty("limit") Integer limit,
                           @JsonProperty("offset") Integer offset,
                           @JsonProperty("selected") List<String> selected) {
        this.cmd = cmd;
        this.limit = limit;
        this.offset = offset;
        this.selected = selected;
    }

    public String getCmd() {
        return cmd;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public List<String> getSelected() {
        return selected;
    }
}
