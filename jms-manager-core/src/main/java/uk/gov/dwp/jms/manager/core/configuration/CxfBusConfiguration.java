package uk.gov.dwp.jms.manager.core.configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;

@Configuration
@Import({
        JacksonConfiguration.class,
        ServiceConfig.class
})
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class CxfBusConfiguration {

    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/*");
    }

    @Bean
    @Autowired
    public Server rsServer(Bus bus, JacksonJsonProvider jacksonJsonProvider, FailedMessageResource failedMessageResource) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setServiceBean(failedMessageResource);
        endpoint.setAddress("/core");
        endpoint.setProvider(jacksonJsonProvider);
        endpoint.setBus(bus);
        return endpoint.create();
    }
}
