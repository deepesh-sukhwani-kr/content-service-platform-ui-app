package com.kroger.csp.ui.config;

import com.kroger.commons.boot.autoconfigure.security.NonSpoofingCondition;
import com.kroger.commons.security.oauth.AbstractOAuth2ClientWebSecurityConfiguration;
import com.kroger.commons.security.oauth.OAuthSecurity;
import com.kroger.csp.ui.util.OauthClientRegistrationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

@Controller
@Configuration
@Conditional(NonSpoofingCondition.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class OAuthClientConfiguration extends AbstractOAuth2ClientWebSecurityConfiguration {
    public static final String[] REQUEST_MATCHERS = {"/", "/index.html", "/login", "/relogin", "/logout", "/oauth/logout", "/api/dance", "/manage/**"};

    @Value("${kroger.oauth.default-target-uri}")
    protected String defaultTargetUrl;

    @Value("${kroger.oauth.logout-uri}")
    protected String logoutUrl;

    @Value("${kroger.rbac.checkRbac}")
    private boolean checkRbac;

    @Autowired
    private RBACConfiguration rbacConfig;

//    protected OAuth2RestOperations oauth2RestOperations;

    @Autowired(required = false)
    @Qualifier("oauthExchangeFilter")
    private static ExchangeFilterFunction oauthExchangeFilter = (request, next) -> next
            .exchange(request);


    // protected OAuth2RestOperations oauth2RestOperations;
    /**
     * Create a rest template to get access_token and logout
     *
     * @param resource
     * @param context
     * @return
     */
//    @Bean
//    public OAuth2RestOperations oauth2RestOperations(
//            OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
//        oauth2RestOperations = new OAuth2RestTemplate(resource, context);
//        return oauth2RestOperations;
//    }
    /*
     * @Bean public OAuth2RestOperations oauth2RestOperations(OAuth2ProtectedResourceDetails
     * resource, OAuth2ClientContext context) { oauth2RestOperations = new
     * OAuth2RestTemplate(resource, context); return oauth2RestOperations; }
     */

    /**
     * Logout end point, invokes logout api on identity server to revoke the token, then
     * forwards to the usual logout handling on this server to clean up the session
     *
     * @return logout page
     */
//    @RequestMapping("/oauth/logout")
//    public String logout(
//            @RequestParam(required = false, value = "targetUrl") String targetUrl) {
//        if (targetUrl == null || targetUrl.isEmpty()) {
//            targetUrl = defaultTargetUrl;
//        }
//        return OAuthUtil.logout(oauth2RestOperations, logoutUrl, targetUrl);
//    }

    /**
     * Override logout handler so that we redirect back into the application
     */
//    @Bean
//    protected LogoutSuccessHandler logoutSuccessHandler() {
//        return OAuthUtil.logoutSuccessHandler(true, defaultTargetUrl);
//    }

    /**
     * This application is a stand-alone web application (as opposed to a micro-service
     * design behind an API gateway).
     * <p>
     * Being an OAuth2 Client, we support login and logout URLs.
     */
    @Override
    protected void configure(OAuthSecurity oAuthSecurity) throws Exception {
        oauthExchangeFilter = oAuthSecurity.filters()
                .servletOAuth2AuthorizedClientExchangeFilterFunction(
                        OauthClientRegistrationEnum.KROGER_SERVICE.getRegistrationId());
        // @formatter:off
//        oAuthSecurity.http().requestMatcher(OAuthSecurity.createFrontEndMatchers(REQUEST_MATCHERS));
//
//        oAuthSecurity
//                .supportLogin()
//                .supportLogout(logoutSuccessHandler());
        oAuthSecurity.frontEndRequestMatchers(REQUEST_MATCHERS).supportOAuth2Login()
                .supportOAuth2Logout();


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
    private void configureAuthRules(HttpSecurity http) throws Exception {
        http.csrf().disable();
        if (checkRbac)
            http.authorizeRequests().antMatchers("/login", "/logout", "/oauth/logout", "/manage/**").permitAll()
                    .antMatchers("/add").access(getAntMatchersRoles(rbacConfig.getAddRoles()))
                    .antMatchers("/csvupload").access(getAntMatchersRoles(rbacConfig.getAddRoles()))
                    .antMatchers("/search").access(getAntMatchersRoles(rbacConfig.getSearchRoles()))
                    .anyRequest().authenticated();
        else
            http.authorizeRequests().antMatchers("/login", "/logout", "/oauth/logout", "/manage/**").permitAll()
                    .anyRequest().authenticated();
    }

    private String getAntMatchersRoles(List<String> roles){
        StringBuffer sb = new StringBuffer();
        roles.forEach(role -> {
            sb.append("hasRole('"+role+"') or ");
        });
        return sb.substring(0, sb.length()- 3);
    }
}