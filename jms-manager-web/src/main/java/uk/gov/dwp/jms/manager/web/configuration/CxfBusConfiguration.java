package uk.gov.dwp.jms.manager.web.configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import uk.gov.dwp.jms.manager.core.client.FailedMessageResource;
import uk.gov.dwp.jms.manager.web.common.jackson.PageMessageBodyWriter;
import uk.gov.dwp.jms.manager.web.common.mustache.MustachePageRenderer;
import uk.gov.dwp.jms.manager.web.search.FailedMessageListController;
import uk.gov.dwp.jms.manager.web.search.FailedMessagesJsonSerializer;

import javax.servlet.ServletException;
import java.util.ArrayList;

@Configuration
@Import({
        JacksonConfiguration.class,
        JmsManagerCoreClientConfiguration.class,
        MustacheConfiguration.class
})
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class CxfBusConfiguration {

    @Bean
    public ServletRegistrationBean cxfServlet() throws ServletException {
        ServletRegistrationBean cxfServlet = new ServletRegistrationBean(new CXFServlet(), "/*");
//        cxfServlet.addInitParameter("redirect-servlet-name", "default");
//        cxfServlet.addInitParameter("redirect-attributes", "javax.servlet.include.request_uri");
//        cxfServlet.addInitParameter("redirects-list", "/static/.+");
        cxfServlet.addInitParameter("static-resources-list", "/static/.+");
        return cxfServlet;
    }

    @Bean
    public Server rsServer(Bus bus,
                           FailedMessageResource failedMessageResource,
                           JacksonConfiguration jacksonConfiguration,
                           MustachePageRenderer mustachePageRenderer) {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setAddress("/web");
        endpoint.setServiceBeans(new ArrayList<Object>() {{
            add(new FailedMessageListController(failedMessageResource, new FailedMessagesJsonSerializer()));
        }});
        endpoint.setProvider(jacksonConfiguration.jacksonJsonProvider());
        endpoint.setProvider(new PageMessageBodyWriter(mustachePageRenderer));
        endpoint.setBus(bus);
        return endpoint.create();
    }
}
