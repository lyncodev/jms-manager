package uk.gov.dwp.jms.manager.core.classification.action;

import client.Destination;
import client.SendMessageRequest;
import client.SendMessageResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.gov.dwp.jms.manager.core.service.replace.StringReplacementService;

@JsonTypeName("modifyAndSend")
public class ModifyAndSendFailedMessageAction implements FailedMessageAction {
    private static final String PATTERN = "pattern";
    private static final String REPLACEMENT = "replacement";
    private static final String DESTINATION = "destination";

    @JsonProperty(PATTERN)
    private final String pattern;
    @JsonProperty(REPLACEMENT)
    private final String replacement;
    @JsonProperty(DESTINATION)
    private final String destination;

    @JsonCreator
    public ModifyAndSendFailedMessageAction(@JsonProperty(PATTERN) String pattern,
                                            @JsonProperty(REPLACEMENT) String replacement,
                                            @JsonProperty(DESTINATION) String destination) {
        this.pattern = pattern;
        this.replacement = replacement;
        this.destination = destination;
    }

    @Override
    public void perform(Request request) {
        StringReplacementService stringReplacementService = request.getApplicationContext().getBean(StringReplacementService.class);
        String newContent = stringReplacementService.replaceRegex(request.getFailedMessage().getContent(), pattern, replacement);

        SendMessageResource sendMessageResource = request.getApplicationContext().getBean(SendMessageResource.class);
        sendMessageResource.sendMessage(createSendMessageRequest(request, newContent));
    }

    private SendMessageRequest createSendMessageRequest(Request request, String newContent) {
        String brokerName = request.getFailedMessage().getDestination().getBrokerName();
        return new SendMessageRequest(
                new Destination(brokerName, destination),
                newContent,
                request.getFailedMessage().getProperties()
        );
    }

}
