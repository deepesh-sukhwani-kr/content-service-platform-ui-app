package com.kroger.csp.ui.config;

import com.kroger.commons.boot.autoconfigure.security.NonSpoofingCondition;
import com.kroger.commons.security.KrogerSecurityProperties;
import com.kroger.commons.security.RethrowExceptionAuthenticationEntryPoint;
import com.kroger.commons.security.oauth.OAuth2ResourceConfigurationSupport;
import com.kroger.commons.security.oauth.OAuthSecurity;
import com.kroger.commons.security.oauth.SessionAwareBearerTokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Order(3)
@Configuration
@Conditional(NonSpoofingCondition.class)
public class OAuthResourceConfiguration extends OAuth2ResourceConfigurationSupport
{
    @Autowired
    private KrogerSecurityProperties krogerSecurityProperties;

    //@Autowired
    //private HandlerExceptionResolverComposite resolver;

    @Value("${kroger.management.security.roles}")
    private String[] managementSecurityRoles;

    /**
     * We do not give out the access token to the browser. Therefore, configure this
     * resource to find the access token in the session if not found in the typical
     * locations.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception
    {
        resources.tokenExtractor(new SessionAwareBearerTokenExtractor());
        resources.authenticationEntryPoint(new RethrowExceptionAuthenticationEntryPoint());
    }

    /**
     * Configuration for an OAuth Resource that requires a session to find the access token.
     */
    @Override
    protected void configure(OAuthSecurity oAuthSecurity) throws Exception
    {
        // Session is required when access token is not given out
        oAuthSecurity
                .http().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        configureAuthRules(oAuthSecurity.http(), krogerSecurityProperties,
                managementSecurityRoles);
    }

    /**
     * The rest api of the application is protected here.
     *
     * This static method is shared with the spoofing configuration.
     *
     * @param http
     * @throws Exception
     */
    public static void configureAuthRules(HttpSecurity http,
                                          KrogerSecurityProperties krogerSecurityProperties,
                                          String[] managementSecurityRoles) throws Exception
    {
        // @formatter:off
        krogerSecurityProperties.permitAllWebResources(http);
        http.authorizeRequests()
                .antMatchers("/api/me", "/api/spoofing").permitAll()
                .antMatchers("/manage/health", "/manage/info", "/manage/keepalive").permitAll()
                .antMatchers("/manage/**").hasAnyRole(managementSecurityRoles)
                .anyRequest().authenticated();
        // @formatter:on
    }
}