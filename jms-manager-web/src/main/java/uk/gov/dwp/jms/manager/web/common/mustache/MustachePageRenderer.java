package uk.gov.dwp.jms.manager.web.common.mustache;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.security.web.csrf.CsrfToken;
import uk.gov.dwp.jms.manager.web.common.Page;
import uk.gov.dwp.jms.manager.web.common.security.SecurityContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MustachePageRenderer {

    private final MustacheFactory mustacheFactory;
    private final SecurityContext securityContext;

    public MustachePageRenderer(MustacheFactory mustacheFactory, SecurityContext securityContext) {
        this.mustacheFactory = mustacheFactory;
        this.securityContext = securityContext;
    }

    public void render(Page page, OutputStream output) throws IOException {
        try {
            final Mustache template = mustacheFactory.compile(page.getTemplate());
            try (OutputStreamWriter writer = new OutputStreamWriter(output, UTF_8)) {
                template.execute(writer, Arrays.asList(
                        page,
                        new Object() {
                            public Callable<CsrfToken> _csrf() {
                                return securityContext::getToken;
                            }
                        },
                        new Object() {
                            public Callable<String> _csrfToken() {
                                return () -> securityContext.getToken().getToken();
                            }
                        },
                        new Object() {
                            public Callable<String> _csrfHeaderName() {
                                return () -> securityContext.getToken().getHeaderName();
                            }
                        }
                ));
            }
        } catch (Throwable e) {
            throw new RuntimeException("Mustache template error: " + page.getTemplate(), e);
        }
    }
}
