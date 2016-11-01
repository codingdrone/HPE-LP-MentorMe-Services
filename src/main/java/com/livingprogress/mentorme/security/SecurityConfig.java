package com.livingprogress.mentorme.security;

import com.livingprogress.mentorme.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

/**
 * The application security config.
 */
@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * The user detail service.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The stateless auth filter.
     */
    @Autowired
    private StatelessAuthenticationFilter statelessAuthenticationFilter;

    /**
     * The password encoder.
     *
     * @return password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return Helper.getPasswordEncoder();
    }

    /**
     * Configure global auth manager builder
     *
     * @param auth the auth manager builder
     * @throws Exception throws if any error happen
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(Helper.getPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authProvider);
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Create auth manager bean.
     *
     * @return the auth manager bean.
     * @throws Exception throws if any error happen
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configure authentication.
     *
     * @param http the http
     * @throws Exception if there is any error
     */
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(statelessAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint((request, response, e) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            })
            .and()
            .anonymous()
            .and()
            .servletApi()
            .and()
            .headers()
            .cacheControl()
            .and()
            .and()
            .authorizeRequests()
            .antMatchers("/").permitAll()
                //allow anonymous for lookup requests
                .antMatchers("/favicon.ico")
                .permitAll()
                .antMatchers("/lookups/**")
                .permitAll()
                .antMatchers(POST, "/users")
                .hasAuthority("SYSTEM_ADMIN")
                .antMatchers(DELETE, "/users/{id}")
                .hasAuthority("SYSTEM_ADMIN")
                .antMatchers(POST, "/institutions")
                .hasAuthority("SYSTEM_ADMIN")
                .antMatchers(PUT, "/institutions/{id}")
                .hasAnyAuthority("SYSTEM_ADMIN,INSTITUTION_ADMIN")
                .antMatchers(DELETE, "/institutions/{id}")
                .hasAuthority("SYSTEM_ADMIN")
                .antMatchers(PUT, "/institutions/{id}/generateAffiliationCode")
                .hasAnyAuthority("SYSTEM_ADMIN,INSTITUTION_ADMIN")
                .antMatchers(PUT, "/mentees/{id}")
                .hasAnyAuthority("SYSTEM_ADMIN,MENTEE")
                .antMatchers(DELETE, "/mentees/{id}")
                .hasAnyAuthority("SYSTEM_ADMIN,MENTEE")
                .antMatchers(PUT, "/mentors/{id}")
                .hasAnyAuthority("SYSTEM_ADMIN,MENTOR")
                .antMatchers(DELETE, "/mentors/{id}")
                .hasAnyAuthority("SYSTEM_ADMIN,MENTOR")
                .antMatchers(GET, "/mentors/linkedInExperience").hasAnyAuthority("MENTOR")
                //allow anonymous calls to social login
                .antMatchers("/auth/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }
}

