package uk.gov.dwp.jms.manager.core.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

public class MessageWithPropertiesAdapterListener implements MessageListener {

    private final MessageTextExtractor messageTextExtractor;
    private final MessagePropertyExtractor messagePropertyExtractor;
    private final MessageWithPropertiesListener delegate;

    public MessageWithPropertiesAdapterListener(MessageTextExtractor messageTextExtractor,
                                                MessagePropertyExtractor messagePropertyExtractor,
                                                MessageWithPropertiesListener delegate) {
        this.messageTextExtractor = messageTextExtractor;
        this.messagePropertyExtractor = messagePropertyExtractor;
        this.delegate = delegate;
    }

    @Override
    public void onMessage(Message message) {
        delegate.onMessage(new MessageWithProperties(
                messageTextExtractor.extractText(message),
                messagePropertyExtractor.extractProperties(message)
        ));

    }
}
