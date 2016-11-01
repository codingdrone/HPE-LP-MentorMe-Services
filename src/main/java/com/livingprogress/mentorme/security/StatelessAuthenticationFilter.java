package com.livingprogress.mentorme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * The state less auth filter.
 */
@Component
public class StatelessAuthenticationFilter extends GenericFilterBean {

    /**
     * The token auth service.
     */
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    /**
     * Filter with parsing auth from header.
     * @param request the http servlet request.
     * @param response the http servlet response
     * @param filterChain the filter chain.
     * @throws IOException throws if io error happens.
     * @throws ServletException throws if servlet error happens.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        setAuthenticationFromHeader((HttpServletRequest) request);
        filterChain.doFilter(request, response);
    }

    /**
     * Set auth from header.
     * @param request the http servlet request.
     */
    private void setAuthenticationFromHeader(HttpServletRequest request){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof UserAuthentication)) {
            final UserAuthentication userAuthentication = tokenAuthenticationService.getAuthentication(request);
            if (userAuthentication != null) {
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }
        }
    }
}
