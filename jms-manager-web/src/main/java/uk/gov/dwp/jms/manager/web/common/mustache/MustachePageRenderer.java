package uk.gov.dwp.jms.manager.web.common.mustache;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import uk.gov.dwp.jms.manager.web.common.Page;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MustachePageRenderer {

    private final MustacheFactory mustacheFactory;

    public MustachePageRenderer(MustacheFactory mustacheFactory) {
        this.mustacheFactory = mustacheFactory;
    }

    public void render(Page page, OutputStream output) throws IOException {
        try {
            final Mustache template = mustacheFactory.compile(page.getTemplate());
            try (OutputStreamWriter writer = new OutputStreamWriter(output, UTF_8)) {
                template.execute(writer, page);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Mustache template error: " + page.getTemplate(), e);
        }
    }
}
