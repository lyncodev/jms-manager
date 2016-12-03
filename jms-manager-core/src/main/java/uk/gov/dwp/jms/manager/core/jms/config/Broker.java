package uk.gov.dwp.jms.manager.core.jms.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Broker {
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";

    @JsonProperty(NAME)
    private final String name;
    @JsonProperty(URL)
    private final String url;
    @JsonProperty(PASSWORD)
    private final String password;
    @JsonProperty(USERNAME)
    private final String username;

    @JsonCreator
    public Broker(@JsonProperty(NAME) String name,
                  @JsonProperty(URL) String url,
                  @JsonProperty(PASSWORD) String password,
                  @JsonProperty(USERNAME) String username) {
        this.name = name;
        this.url = url;
        this.password = password;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public ActiveMQConnectionFactory createConnectionFactory () {
        return new ActiveMQConnectionFactory(username, password, url);
    }
}
