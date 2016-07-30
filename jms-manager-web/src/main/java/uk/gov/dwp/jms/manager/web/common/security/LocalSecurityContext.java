package uk.gov.dwp.jms.manager.web.common.security;

import org.springframework.security.web.csrf.CsrfToken;

public class LocalSecurityContext implements SecurityContext {

    private final ThreadLocal<CsrfToken> tokens = new ThreadLocal<>();

    @Override
    public CsrfToken getToken() {
        return tokens.get();
    }

    @Override
    public void setToken(CsrfToken token) {
        tokens.set(token);
    }

    @Override
    public void clear() {
        tokens.remove();
    }
}
