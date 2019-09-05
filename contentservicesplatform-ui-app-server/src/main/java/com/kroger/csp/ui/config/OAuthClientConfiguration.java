package com.kroger.csp.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kroger.commons.boot.autoconfigure.security.NonSpoofingCondition;
import com.kroger.commons.security.oauth.OAuth2ClientConfigurationSupport;
import com.kroger.commons.security.oauth.OAuthSecurity;
import com.kroger.commons.security.oauth.OAuthUtil;

@Controller
@Configuration
@Conditional(NonSpoofingCondition.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class OAuthClientConfiguration extends OAuth2ClientConfigurationSupport
{
    public static final String[] REQUEST_MATCHERS = {"/", "/index.html", "/login", "/relogin", "/logout", "/oauth/logout", "/api/dance"};

    @Value("${kroger.oauth.default-target-uri}")
    protected String defaultTargetUrl;

    @Value("${kroger.oauth.logout-uri}")
    protected String logoutUrl;

    protected OAuth2RestOperations oauth2RestOperations;

    /**
     * Create a rest template to get access_token and logout
     *
     * @param resource
     * @param context
     * @return
     */
    @Bean
    public OAuth2RestOperations oauth2RestOperations(
            OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context)
    {
        oauth2RestOperations = new OAuth2RestTemplate(resource, context);
        return oauth2RestOperations;
    }

    /**
     * Logout end point, invokes logout api on identity server to revoke the token, then
     * forwards to the usual logout handling on this server to clean up the session
     *
     * @return logout page
     */
    @RequestMapping("/oauth/logout")
    public String logout(
            @RequestParam(required = false, value = "targetUrl") String targetUrl)
    {
        if (targetUrl == null || targetUrl.isEmpty())
        {
            targetUrl = defaultTargetUrl;
        }
        return OAuthUtil.logout(oauth2RestOperations, logoutUrl, targetUrl);
    }

    /**
     * Override logout handler so that we redirect back into the application
     */
    @Bean
    protected LogoutSuccessHandler logoutSuccessHandler()
    {
        return OAuthUtil.logoutSuccessHandler(true, defaultTargetUrl);
    }

    /**
     * This application is a stand-alone web application (as opposed to a micro-service
     * design behind an API gateway).
     *
     * Being an OAuth2 Client, we support login and logout URLs.
     */
    @Override
    protected void configure(OAuthSecurity oAuthSecurity) throws Exception
    {
        // @formatter:off
        oAuthSecurity.http().requestMatcher(OAuthSecurity.createFrontEndMatchers(REQUEST_MATCHERS));

        oAuthSecurity
                .supportLogin()
                .supportLogout(logoutSuccessHandler());

        configureAuthRules(oAuthSecurity.http());
        // @formatter:on
    }

    /**
     * This static method is shared with the spoofing configuration. Anyone can access the
     * login urls, but must be authenticated before accessing this application, including
     * index.html
     *
     * @param http
     * @throws Exception
     */
    public static void configureAuthRules(HttpSecurity http) throws Exception
    {
        http.csrf().disable();
        // @formatter:off
        http.authorizeRequests().antMatchers("/login", "/logout", "/oauth/logout").permitAll()
                .anyRequest().authenticated();
        // @formatter:on
    }
}