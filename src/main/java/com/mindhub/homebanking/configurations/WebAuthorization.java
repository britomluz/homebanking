package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import javax.servlet.http.*;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(){
        ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionAuthenticationStrategy.setMaximumSessions(1);
        sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(true);
        return sessionAuthenticationStrategy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/shop.html", "/web/index.js", "/error/**", "/web/style/**", "/web/scripts/**", "/web/assets/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/shop").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/web/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/manager.html", "/h2-console", "/rest/**", "/api/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/newloans", "/api/admin/**").hasAuthority("ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .successHandler((req, res, auth) -> clearAuthenticationAttributes(req))
                .failureHandler((req, res, exc) -> {
                    if(exc.getMessage().contains("Maximum sessions of 1 for this principal exceeded")){
                        res.sendError(HttpServletResponse.SC_CONFLICT, "Ya tienes una sesion activa");
                    } else{
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                });


        http.logout()
                .logoutUrl("/api/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        http.sessionManagement()
                .invalidSessionUrl("/web/index.html")
                //.invalidSessionUrl("/login?expired=true")
                //.invalidSessionUrl("/api/?timeout=true")
                .maximumSessions(8)
                //.expiredUrl("/web/index.html")
                .expiredUrl("/api/login?expired=true")
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());

               // .maximumSessions(2)
                //.expiredUrl("/logout?timeout")
               // .maxSessionsPreventsLogin(true);

       // turn off checking for CSRF tokens

        http.csrf().disable();

        http.cors();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendRedirect("/web/index.html"));
        //http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

       // http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

       // http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

       // http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            session.setMaxInactiveInterval(600);
        }
    }

    /* we need this bean for the session managment.
    * Specially if we want to control the concurrent session-control support.
    * with spring security @return
    * */

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
         return new HttpSessionEventPublisher();
    }

    /* with every session event this publisher will publish the event
    * and then Spring security uses those events to take a call
    * wether you should be allow to log in or how many sessions we have*/
}
