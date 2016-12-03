package uk.gov.dwp.jms.manager.core.service.replace;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StringReplacementServiceTest {
    private StringReplacementService underTest = StringReplacementService.stringReplaceService();

    @Test
    public void replaceRegex() throws Exception {
        String result = underTest.replaceRegex("One five three", ".* (\\w+) .*", "Four $1 six");
        assertThat(result, is("Four five six"));
    }
}