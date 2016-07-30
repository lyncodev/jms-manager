package uk.gov.dwp.jms.manager.web.common;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.dwp.jms.manager.web.common.security.SecurityContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfTokenResponseHeaderFilter extends OncePerRequestFilter {

    private static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName().concat(".CSRF_TOKEN");
    private static final String RESPONSE_HEADER_NAME = "X-CSRF-HEADER";
    private static final String RESPONSE_PARAM_NAME = "X-CSRF-PARAM";
    private static final String RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN";

    private final SecurityContext securityContext;

    public CsrfTokenResponseHeaderFilter(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token != null) {
            response.setHeader(RESPONSE_HEADER_NAME, token.getHeaderName());
            response.setHeader(RESPONSE_PARAM_NAME, token.getParameterName());
            response.setHeader(RESPONSE_TOKEN_NAME , token.getToken());
            securityContext.setToken(token);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            securityContext.clear();
        }
    }
}
