package uk.gov.dwp.jms.manager.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import uk.gov.dwp.jms.manager.web.common.CsrfTokenResponseHeaderFilter;
import uk.gov.dwp.jms.manager.web.common.security.LocalSecurityContext;
import uk.gov.dwp.jms.manager.web.common.security.SecurityContext;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().formLogin()
                .and().logout().logoutUrl("/web/logout")
        ;
        http.addFilterAfter(new CsrfTokenResponseHeaderFilter(securityContext()), CsrfFilter.class);
    }

    @Bean
    public SecurityContext securityContext() {
        return new LocalSecurityContext();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/static/**");
    }

    @Configuration
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .ldapAuthentication()
                    .userDnPatterns("uid={0},ou=people")
                    .groupSearchBase("ou=groups")
                    .contextSource().ldif("classpath:test-server.ldif");
        }
    }
}