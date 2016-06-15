package uk.gov.dwp.jms.manager.web.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.dwp.jms.manager.web.w2ui.BaseW2UIRequest;

import java.util.List;

public class LabelRequest extends BaseW2UIRequest {

    public final List<Change> changes;

    public LabelRequest(@JsonProperty("cmd") String cmd,
                        @JsonProperty("limit") Integer limit,
                        @JsonProperty("offset") Integer offset,
                        @JsonProperty("selected") List<String> selected,
                        @JsonProperty("changes") List<Change> changes) {
        super(cmd, limit, offset, selected);
        this.changes = changes;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public static class Change {

        public final String recid;
        public final String labels;

        public Change(@JsonProperty("recid") String recid,
                      @JsonProperty("labels") String labels) {
            this.recid = recid;
            this.labels = labels;
        }

        public String getRecid() {
            return recid;
        }

        public String getLabels() {
            return labels;
        }
    }
}
