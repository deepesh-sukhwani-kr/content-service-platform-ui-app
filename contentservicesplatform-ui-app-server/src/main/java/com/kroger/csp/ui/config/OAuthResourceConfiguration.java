package com.kroger.csp.ui.config;

import com.kroger.commons.boot.autoconfigure.security.NonSpoofingCondition;
import com.kroger.commons.security.KrogerSecurityProperties;
import com.kroger.commons.security.oauth.AbstractOAuth2ResourceWebSecurityConfiguration;
import com.kroger.commons.security.oauth.CheckOpaqueTokenFilter;
import com.kroger.commons.security.oauth.KrogerSecurityMiscellany;
import com.kroger.commons.security.oauth.OAuthSecurity;
import com.kroger.csp.ui.util.OauthClientRegistrationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

@Order(3)
@Configuration
@Conditional(NonSpoofingCondition.class)
public class OAuthResourceConfiguration extends AbstractOAuth2ResourceWebSecurityConfiguration
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
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception
//    {
//        resources.tokenExtractor(new SessionAwareBearerTokenExtractor());
//        resources.authenticationEntryPoint(new RethrowExceptionAuthenticationEntryPoint());
//    }
    @Autowired(required = false)
    @Qualifier("oauthExchangeFilter")
    protected ExchangeFilterFunction oauthExchangeFilter = (request, next) -> next
            .exchange(request);

    /**
     * Configuration for an OAuth Resource that requires a session to find the access token.
     */
    @Override
    protected void configure(OAuthSecurity oAuthSecurity) throws Exception
    {
        // Session is required when access token is not given out
        oauthExchangeFilter = oAuthSecurity.filters()
                .servletOAuth2AuthorizedClientExchangeFilterFunction(
                        OauthClientRegistrationEnum.KROGER_SERVICE.getRegistrationId());
        // some reads operations are done via POST, so we need to give read access to
        // them too
        CheckOpaqueTokenFilter checkOpaqueTokenFilter = new CheckOpaqueTokenFilter(
                this.securityMiscellany());
        oAuthSecurity.http().addFilterAfter(checkOpaqueTokenFilter,
                SecurityContextPersistenceFilter.class);
        KrogerSecurityMiscellany securityMiscellany = securityMiscellany();

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