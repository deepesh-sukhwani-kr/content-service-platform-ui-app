package com.kroger.csp.ui.config;

import com.kroger.commons.boot.autoconfigure.security.SpoofingCondition;
import com.kroger.commons.security.KrogerSecurityProperties;
import com.kroger.commons.security.RequestMatcherConfigurer;
import com.kroger.commons.security.oauth.OAuthSecurity;
import com.kroger.commons.security.spoofing.SpoofingConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Configuration
@Conditional(SpoofingCondition.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SpoofingClientConfiguration extends SpoofingConfigurerAdapter
{
    @Autowired
    private KrogerSecurityProperties krogerSecurityProperties;

    /**
     * Support end point to mimic OAuth2 logout config
     *
     * @return logout page
     */
    @RequestMapping("/oauth/logout")
    public String logout()
    {
        return "redirect:/logout";
    }

    @Override
    public void match(RequestMatcherConfigurer matchers)
    {
        matchers.requestMatchers(OAuthSecurity.createFrontEndMatchers(OAuthClientConfiguration.REQUEST_MATCHERS));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        krogerSecurityProperties.enableCookieCsrfTokenRepository(http);

        // Use the same rules as OAuth client configuration
        OAuthClientConfiguration.configureAuthRules(http);
    }
}
