package uk.gov.dwp.jms.manager.web.common;

public class Page {

    private final String template;

    public Page(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
