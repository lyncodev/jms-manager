package uk.gov.dwp.jms.manager.core.classification.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.dwp.jms.manager.core.dao.FailedMessageLabelsDao;

@JsonTypeName("name")
public class LabelFailedMessageAction implements FailedMessageAction {
    private static final String NAME = "name";

    @JsonProperty(NAME)
    private final String name;

    @JsonCreator
    public LabelFailedMessageAction(@JsonProperty(NAME) String name) {
        this.name = name;
    }

    @Override
    public void perform(Request request) {
        FailedMessageLabelsDao failedMessageLabelsDao = request.getApplicationContext().getBean(FailedMessageLabelsDao.class);
        failedMessageLabelsDao.addLabel(request.getFailedMessage().getFailedMessageId(), name);
    }
}
