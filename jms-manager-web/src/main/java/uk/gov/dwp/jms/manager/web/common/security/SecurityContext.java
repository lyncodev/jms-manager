package uk.gov.dwp.jms.manager.web.common.security;

import org.springframework.security.web.csrf.CsrfToken;

public interface SecurityContext {
    CsrfToken getToken();

    void setToken(CsrfToken token);

    void clear();
}
