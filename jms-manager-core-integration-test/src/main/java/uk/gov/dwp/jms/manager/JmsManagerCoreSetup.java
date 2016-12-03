package uk.gov.dwp.jms.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.cxf.helpers.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import uk.gov.dwp.jms.manager.core.JmsManagerApplication;
import uk.gov.dwp.jms.manager.client.jackson.JacksonObjectMapperFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class JmsManagerCoreSetup {
    private static final ObjectMapper objectMapper = JacksonObjectMapperFactory.create();
    private final JmsManagerCoreConfiguration configuration;
    private ConfigurableApplicationContext context;

    public JmsManagerCoreSetup(JmsManagerCoreConfiguration configuration) {
        this.configuration = configuration;
    }

    public void start () throws Exception {
        clearDatabase();

        File jmsManagerClassifiers = FileUtils.createTempFile("jmsManagerClassifiers", ".json");
        File jmsManagerBrokers = FileUtils.createTempFile("jmsManagerBrokers", ".json");

        if (!jmsManagerClassifiers.exists()) throw new IllegalStateException();
        if (!jmsManagerBrokers.exists()) throw new IllegalStateException();

        try (OutputStream outputStream = new FileOutputStream(jmsManagerClassifiers)) {
            objectMapper.writeValue(outputStream, configuration.getClassifiers());
        }
        try (OutputStream outputStream = new FileOutputStream(jmsManagerBrokers)) {
            objectMapper.writeValue(outputStream, configuration.getBrokers());
        }

        this.context = SpringApplication.run(JmsManagerApplication.class,
                "--server.port=0"
                , "--classifiers.location="+jmsManagerClassifiers.getAbsolutePath()
                , "--brokers.location="+jmsManagerBrokers.getAbsolutePath()
                , "--spring.config.location=../jms-manager-core/app.properties"
                , "--jms.manager.db.dbName="+configuration.getDatabaseName()
                , "--spring.data.mongodb.uri="+configuration.getMongoClientUri()
        );
        this.context.start();
    }

    private void clearDatabase() {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(configuration.getMongoClientUri()));
        mongoClient.dropDatabase(configuration.getDatabaseName());
        mongoClient.close();
    }

    public void stop () {
        this.context.stop();
        clearDatabase();
    }

    public int getPort () {
        return ((EmbeddedWebApplicationContext) this.context).getEmbeddedServletContainer().getPort();
    }

    public <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }
}
