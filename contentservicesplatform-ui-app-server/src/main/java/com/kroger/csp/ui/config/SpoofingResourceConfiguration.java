package com.kroger.csp.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.kroger.commons.boot.autoconfigure.security.SpoofingCondition;
import com.kroger.commons.security.KrogerSecurityProperties;
import com.kroger.commons.security.RethrowExceptionAuthenticationEntryPoint;

@Order(6)
@Configuration
@Conditional(SpoofingCondition.class)
public class SpoofingResourceConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    private KrogerSecurityProperties krogerSecurityProperties;

    @Value("${kroger.management.security.roles}")
    private String[] managementSecurityRoles;

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        krogerSecurityProperties.enableCookieCsrfTokenRepository(http);

        http.exceptionHandling().authenticationEntryPoint(new RethrowExceptionAuthenticationEntryPoint());
        // Use the same rules as OAuth resource configuration
        OAuthResourceConfiguration.configureAuthRules(http, krogerSecurityProperties,
                managementSecurityRoles);
    }
}
