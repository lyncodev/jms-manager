package uk.gov.dwp.jms.manager.core.jms;

import javax.jms.Message;
import java.util.Map;

public interface MessagePropertyExtractor {

    Map<String, Object> extractProperties(Message message);
}
