package uk.gov.dwp.jms.manager.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jms.manager.db")
public class DaoProperties {

    public static class Collection {

        private String failedMessage;
        private String failedMessageLabel;

        public String getFailedMessage() {
            return failedMessage;
        }

        public void setFailedMessage(String failedMessage) {
            this.failedMessage = failedMessage;
        }

        public String getFailedMessageLabel() {
            return failedMessageLabel;
        }

        public void setFailedMessageLabel(String failedMessageLabel) {
            this.failedMessageLabel = failedMessageLabel;
        }
    }

    private String dbName;
    private Collection collection;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
